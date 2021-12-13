package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class DriverDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private DriverDao driverDao = new DriverDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(driverDao.getTableName().contains(".driver"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "driver.id",
                "driver.driver_license",
                "driver.taxi_id",
                "driver.status_name"
        );

        assertEquals(driverDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        Driver driver = new Driver(
                BuberUser.with().account(new Account(1L, null, null, null)).build(),
                "2FF 441144",
                new Taxi(1L, null, null, null, null, null),
                DriverStatus.REST
        );

        columnsByValues.put("driver.id", driver.getId().get());
        columnsByValues.put("driver.driver_license", driver.getDrivingLicence());
        columnsByValues.put("driver.taxi_id", driver.getTaxi().getId().get());
        columnsByValues.put("driver.status_name", driver.getDriverStatus().name());

        assertEquals(driverDao.getColumnsAndValuesToBeInserted(driver), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(driverDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        Driver driver = new Driver(
                BuberUser.with().account(new Account(1L, null, null, null)).build(),
                "2FF 441144",
                new Taxi(1L, null, null, null, null, null),
                DriverStatus.REST
        );

        try {
            when(resultSet.getLong("driver.id")).thenReturn(1L);
            when(resultSet.getString("driver.driver_license")).thenReturn("2FF 441144");
            when(resultSet.getLong("driver.taxi_id")).thenReturn(1L);
            when(resultSet.getString("driver.status_name")).thenReturn("REST");

            assertEquals(driverDao.extractResult(resultSet), driver);
            verify(resultSet).getLong("driver.id");
            verify(resultSet).getString("driver.driver_license");
            verify(resultSet).getLong("driver.taxi_id");
            verify(resultSet).getString("driver.status_name");
        } catch (SQLException ignored) {
        }
    }

}