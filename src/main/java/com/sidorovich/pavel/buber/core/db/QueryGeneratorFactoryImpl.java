package com.sidorovich.pavel.buber.core.db;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;

public class QueryGeneratorFactoryImpl implements QueryGeneratorFactory {

    private QueryGeneratorFactoryImpl() {
    }

    @Override
    public QueryGenerator of(ConnectionPool connectionPool) {
        return new QueryGeneratorImpl(connectionPool);
    }

    public static QueryGeneratorFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final QueryGeneratorFactoryImpl INSTANCE = new QueryGeneratorFactoryImpl();
    }

}
