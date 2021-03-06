package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.dao.EntityDao;
import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.service.ServiceFactory;
import com.sidorovich.pavel.buber.core.db.QueryGeneratorFactoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DaoFactory implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";

    private final Map<Class<?>, EntityDao<?>> daoByEntity = new ConcurrentHashMap<>();
    private final ConnectionPool connectionPool = ConnectionPool.locking();
    private final QueryGeneratorFactory queryGeneratorFactory = QueryGeneratorFactoryImpl.getInstance();

    private DaoFactory() {
    }

    private static class Holder {
        private static final DaoFactory INSTANCE = new DaoFactory();
    }

    public static DaoFactory getInstance() {
        return Holder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R serviceFor(Class<R> modelClass) {
        return (R) daoByEntity.computeIfAbsent(modelClass, createDaoFromClass());
    }

    private Function<Class<?>, EntityDao<?>> createDaoFromClass() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
            case "AccountDao":
                return new AccountDao(connectionPool, queryGeneratorFactory);
            case "BonusDao":
                return new BonusDao(connectionPool, queryGeneratorFactory);
            case "UserDao":
                return new UserDao(connectionPool, queryGeneratorFactory);
            case "CoordinatesDao":
                return new CoordinatesDao(connectionPool, queryGeneratorFactory);
            case "DriverDao":
                return new DriverDao(connectionPool, queryGeneratorFactory);
            case "TaxiDao":
                return new TaxiDao(connectionPool, queryGeneratorFactory);
            case "OrderDao":
                return new OrderDao(connectionPool, queryGeneratorFactory);
            default:
                throw new IllegalArgumentException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

}
