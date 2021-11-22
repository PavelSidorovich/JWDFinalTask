package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.api.service.ServiceFactory;
import com.sidorovich.pavel.buber.core.dao.AccountDao;
import com.sidorovich.pavel.buber.core.dao.BuberUserDao;
import com.sidorovich.pavel.buber.core.dao.CoordinatesDao;
import com.sidorovich.pavel.buber.core.dao.DaoFactory;
import com.sidorovich.pavel.buber.core.dao.DriverDao;
import com.sidorovich.pavel.buber.core.dao.TaxiDao;
import com.sidorovich.pavel.buber.core.dao.UserOrderDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class EntityServiceFactory implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";

    private final Map<Class<?>, EntityService<?>> serviceByEntity = new ConcurrentHashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private EntityServiceFactory() {
    }

    private static class Holder {
        private static final EntityServiceFactory INSTANCE = new EntityServiceFactory();
    }

    public static EntityServiceFactory getInstance() {
        return Holder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R serviceFor(Class<R> modelClass) {
        return (R) serviceByEntity.computeIfAbsent(modelClass, createServiceFromClass());
    }

    private Function<Class<?>, EntityService<?>> createServiceFromClass() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
//            case "AccountDao":
//                return new AccountDao(connectionPool);
//            case "BonusDao":
//                return new BonusDao(connectionPool);
            case "BuberUserService":
                return new BuberUserService(daoFactory.serviceFor(BuberUserDao.class),
                                            daoFactory.serviceFor(AccountDao.class));
//            case "CoordinatesDao":
//                return new CoordinatesDao(connectionPool);
            case "DriverService":
                return new DriverService(daoFactory.serviceFor(DriverDao.class),
                                         serviceFor(BuberUserService.class),
                                         daoFactory.serviceFor(TaxiDao.class));
//            case "TaxiDao":
//                return new TaxiDao(connectionPool);
            case "UserOrderService":
                return new UserOrderService(daoFactory.serviceFor(UserOrderDao.class),
                                            serviceFor(DriverService.class),
                                            serviceFor(BuberUserService.class),
                                            daoFactory.serviceFor(CoordinatesDao.class));
            default:
                throw new IllegalArgumentException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

}
