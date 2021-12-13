package com.sidorovich.pavel.buber.api.db;

import com.sidorovich.pavel.buber.core.db.LockingConnectionPool;
import com.sidorovich.pavel.buber.api.exception.CouldNotInitializeConnectionPool;

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
