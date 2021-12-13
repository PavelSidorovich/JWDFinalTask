package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Coordinates;
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

public class TaxiDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private TaxiDao taxiDao = new TaxiDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(taxiDao.getTableName().contains(".taxi"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "taxi.id",
                "taxi.car_brand",
                "taxi.car_model",
                "taxi.car_photo_path",
                "taxi.license_plate",
                "taxi.last_coordinates_id"
        );

        assertEquals(taxiDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        Taxi taxi = new Taxi(
                1L, "Volkswagen", "Polo", "1111 AX-5",
                "file.jpg", new Coordinates(10L, null, null)
        );

        columnsByValues.put("taxi.car_brand", taxi.getCarBrand());
        columnsByValues.put("taxi.car_model", taxi.getCarModel());
        columnsByValues.put("taxi.car_photo_path", taxi.getPhotoFilepath());
        columnsByValues.put("taxi.license_plate", taxi.getLicencePlate());
        columnsByValues.put("taxi.last_coordinates_id", taxi.getLastCoordinates().getId().get());

        assertEquals(taxiDao.getColumnsAndValuesToBeInserted(taxi), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(taxiDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        Taxi taxi = new Taxi(
                1L, "Volkswagen", "Polo", "1111 AX-5",
                "file.jpg", new Coordinates(10L, null, null)
        );

        try {
            when(resultSet.getLong("taxi.id")).thenReturn(1L);
            when(resultSet.getString("taxi.car_brand")).thenReturn("Volkswagen");
            when(resultSet.getString("taxi.car_model")).thenReturn("Polo");
            when(resultSet.getString("taxi.car_photo_path")).thenReturn("file.jpg");
            when(resultSet.getString("taxi.license_plate")).thenReturn("1111 AX-5");
            when(resultSet.getLong("taxi.last_coordinates_id")).thenReturn(10L);

            assertEquals(taxiDao.extractResult(resultSet), taxi);
            verify(resultSet).getLong("taxi.id");
            verify(resultSet).getString("taxi.car_brand");
            verify(resultSet).getString("taxi.car_model");
            verify(resultSet).getString("taxi.car_photo_path");
            verify(resultSet).getString("taxi.license_plate");
            verify(resultSet).getLong("taxi.last_coordinates_id");
        } catch (SQLException ignored) {
        }
    }

}