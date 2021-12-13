package com.sidorovich.pavel.buber.api.db;

public interface QueryGeneratorFactory {

    QueryGenerator of(ConnectionPool connectionPool);

}
