package com.sidorovich.pavel.buber.db;

import com.sidorovich.pavel.buber.exception.CouldNotInitializeConnectionPool;

import java.sql.Connection;

public interface ConnectionPool {

    boolean isInitialized() throws CouldNotInitializeConnectionPool;

    boolean init();

    boolean shutDown();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

    static ConnectionPool locking() {
        return LockingConnectionPool.getInstance();
    }

}
