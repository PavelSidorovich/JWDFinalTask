package com.sidorovich.pavel.buber.db.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.CouldNotInitializeConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    // TODO: 10/16/2021 to move into prop file
    private static final String DB_URL = "jdbc:mysql://localhost:3306/buber";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private final Queue<ProxyConnection> availableConnections = new ConcurrentLinkedDeque<>();
    private final List<ProxyConnection> givenAwayConnections = new CopyOnWriteArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition freeConnections = lock.newCondition();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private LockingConnectionPool() {
    }

    private static class InstanceCreator {
        static LockingConnectionPool INSTANCE = new LockingConnectionPool();
    }

    public static LockingConnectionPool getInstance() {
        return InstanceCreator.INSTANCE;
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
        try {
            lock.lock();
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
        try {
            lock.lock();
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
        try {
            lock.lock();
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
                        .getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
