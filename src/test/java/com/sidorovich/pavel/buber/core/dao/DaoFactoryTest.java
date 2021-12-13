package com.sidorovich.pavel.buber.core.dao;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DaoFactoryTest {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void serviceFor_shouldReturnService_whenClassIsValid() {
        assertNotNull(daoFactory.serviceFor(AccountDao.class));
        assertNotNull(daoFactory.serviceFor(BonusDao.class));
        assertNotNull(daoFactory.serviceFor(UserDao.class));
        assertNotNull(daoFactory.serviceFor(CoordinatesDao.class));
        assertNotNull(daoFactory.serviceFor(DriverDao.class));
        assertNotNull(daoFactory.serviceFor(TaxiDao.class));
        assertNotNull(daoFactory.serviceFor(OrderDao.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void serviceFor_shouldThrowIllegalArgumentException_whenClassIsInValid() {
        daoFactory.serviceFor(String.class);
    }

}