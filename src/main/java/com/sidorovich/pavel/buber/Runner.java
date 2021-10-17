package com.sidorovich.pavel.buber;

import com.sidorovich.pavel.buber.db.ConnectionPool;

import java.sql.SQLException;

public class Runner {

    private final static ConnectionPool CONNECTION_POOL = ConnectionPool.locking();

    public static void main(String[] args) throws InterruptedException, SQLException {

    }
}
