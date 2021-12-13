package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class CoordinatesDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private CoordinatesDao coordinatesDao = new CoordinatesDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(coordinatesDao.getTableName().contains(".coordinate"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "coordinate.id",
                "coordinate.latitude",
                "coordinate.longitude"
        );

        assertEquals(coordinatesDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        Coordinates coordinates = new Coordinates(1L, new BigDecimal(12), new BigDecimal(12));

        columnsByValues.put("coordinate.latitude", coordinates.getLatitude());
        columnsByValues.put("coordinate.longitude", coordinates.getLongitude());

        assertEquals(coordinatesDao.getColumnsAndValuesToBeInserted(coordinates), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(coordinatesDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        Coordinates coordinates = new Coordinates(1L, new BigDecimal(12), new BigDecimal(12));

        try {
            when(resultSet.getLong("coordinate.id")).thenReturn(1L);
            when(resultSet.getBigDecimal("coordinate.latitude")).thenReturn(new BigDecimal(12));
            when(resultSet.getBigDecimal("coordinate.longitude")).thenReturn(new BigDecimal(12));

            assertEquals(coordinatesDao.extractResult(resultSet), coordinates);
            verify(resultSet).getLong("coordinate.id");
            verify(resultSet).getBigDecimal("coordinate.latitude");
            verify(resultSet).getBigDecimal("coordinate.longitude");
        } catch (SQLException ignored) {
        }
    }

}