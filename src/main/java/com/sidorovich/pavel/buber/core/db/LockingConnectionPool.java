package com.sidorovich.pavel.buber.core.db;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.exception.CouldNotInitializeConnectionPool;
import com.sidorovich.pavel.buber.api.exception.CouldNotInitializeConnectionPoolError;
import com.sidorovich.pavel.buber.core.util.PropertyWrapper;
import com.sidorovich.pavel.buber.core.util.ResourcePathExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockingConnectionPool implements ConnectionPool {

    private static final Logger LOG = LogManager.getLogger(LockingConnectionPool.class);

    private static final int INITIAL_CONNECTIONS_AMOUNT = 8;
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_PROPERTIES_FILENAME = "db.properties";

    private final Queue<ProxyConnection> availableConnections = new ConcurrentLinkedDeque<>();
    private final List<ProxyConnection> givenAwayConnections = new CopyOnWriteArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition freeConnections = lock.newCondition();
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final PropertyWrapper propertyWrapper;

    private LockingConnectionPool() {
        try {
            propertyWrapper = new PropertyWrapper(ResourcePathExtractor.getInstance().extract(DB_PROPERTIES_FILENAME));
        } catch (IOException e) {
            LOG.error(e);
            throw new CouldNotInitializeConnectionPoolError(e.getMessage());
        }
    }

    private static class Holder {
        private final static LockingConnectionPool INSTANCE = new LockingConnectionPool();
    }

    public static LockingConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (!initialized.get()) {
                initializeConnections(INITIAL_CONNECTIONS_AMOUNT, true);
                initialized.set(true);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean shutDown() {
        lock.lock();
        try {
            if (initialized.get()) {
                closeConnections();
                deregisterDrivers();
                initialized.set(false);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        lock.lock();
        try {
            while (availableConnections.isEmpty()) {
                freeConnections.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            givenAwayConnections.add(connection);
            return connection;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void returnConnection(Connection connection) {
        lock.lock();
        try {
            if (givenAwayConnections.remove(connection)) {
                availableConnections.add((ProxyConnection) connection);
                freeConnections.signalAll();
            } else {
                LOG.warn("Attempt to add unknown connection to connection pool. Connection: {}", connection);
            }
        } finally {
            lock.unlock();
        }
    }

    private void initializeConnections(int amount, boolean failOnConnectionException) {
        try {
            for (int i = 0; i < amount; i++) {
                final Connection conn = DriverManager
                        .getConnection(
                                propertyWrapper.getProperty(DB_URL),
                                propertyWrapper.getProperty(DB_USER),
                                propertyWrapper.getProperty(DB_PASSWORD)
                        );
                LOG.info("Initialized connection {}", conn);
                final ProxyConnection proxyConnection = new ProxyConnection(conn, this);
                availableConnections.add(proxyConnection);
            }
        } catch (SQLException e) {
            LOG.error("Error occurred while creating connection");
            if (failOnConnectionException) {
                throw new CouldNotInitializeConnectionPool("Failed to create connection", e);
            }
        }
    }

    private void closeConnections() {
        closeConnections(this.availableConnections);
        closeConnections(this.givenAwayConnections);
    }

    private void closeConnections(Collection<ProxyConnection> connections) {
        for (ProxyConnection conn : connections) {
            closeConnection(conn);
        }
    }

    private void closeConnection(ProxyConnection connection) {
        try {
            connection.realClose();
            LOG.info("Closed connection {}", connection);
        } catch (SQLException e) {
            LOG.error("Could not close connection", e);
        }
    }

    private static void deregisterDrivers() {
        LOG.trace("Unregistering SQL drivers");
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOG.error("Could not unregister driver", e);
            }
        }
    }
}
