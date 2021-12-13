package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.model.UserStatus;
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

public class OrderDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private OrderDao orderDao = new OrderDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(orderDao.getTableName().contains(".order"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "order.id",
                "order.client_id",
                "order.driver_id",
                "order.price",
                "order.initial_coordinates_id",
                "order.end_coordinates_id",
                "order.status_name",
                "order.date_of_trip"
        );

        assertEquals(orderDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        UserOrder order =
                UserOrder.with()
                         .client(BuberUser.with().account(
                                                  new Account(1L, null, null, null))
                                          .build())
                         .driver(new Driver(BuberUser.with().account(
                                                             new Account(2L, null, null, null))
                                                     .build(), null, null, null))
                         .id(1L)
                         .endCoordinates(new Coordinates(1L, null, null))
                         .initialCoordinates(new Coordinates(1L, null, null))
                         .price(BigDecimal.ONE)
                         .status(OrderStatus.COMPLETED)
                         .dateOfTrip(Date.valueOf(LocalDate.of(2021, 12, 10)))
                         .build();

        columnsByValues.put("order.client_id", order.getClient().getId().get());
        columnsByValues.put("order.driver_id", order.getDriver().getId().get());
        columnsByValues.put("order.price", order.getPrice());
        columnsByValues.put("order.initial_coordinates_id", order.getInitialCoordinates().getId().get());
        columnsByValues.put("order.end_coordinates_id", order.getEndCoordinates().getId().get());
        columnsByValues.put("order.status_name", order.getStatus().name());
        columnsByValues.put("order.date_of_trip", order.getDateOfTrip());

        assertEquals(orderDao.getColumnsAndValuesToBeInserted(order), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(orderDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        UserOrder order =
                UserOrder.with()
                         .client(BuberUser.with().account(
                                                  new Account(1L, null, null, null))
                                          .build())
                         .driver(new Driver(BuberUser.with().account(
                                                             new Account(2L, null, null, null))
                                                     .build(), null, null, null))
                         .id(1L)
                         .endCoordinates(new Coordinates(6L, null, null))
                         .initialCoordinates(new Coordinates(5L, null, null))
                         .price(BigDecimal.ONE)
                         .status(OrderStatus.COMPLETED)
                         .dateOfTrip(Date.valueOf(LocalDate.of(2021, 12, 10)))
                         .build();

        try {
            when(resultSet.getLong("order.id")).thenReturn(1L);
            when(resultSet.getLong("order.client_id")).thenReturn(1L);
            when(resultSet.getLong("order.driver_id")).thenReturn(2L);
            when(resultSet.getBigDecimal("order.price")).thenReturn(BigDecimal.ONE);
            when(resultSet.getLong("order.initial_coordinates_id")).thenReturn(5L);
            when(resultSet.getLong("order.end_coordinates_id")).thenReturn(6L);
            when(resultSet.getString("order.status_name")).thenReturn("COMPLETED");
            when(resultSet.getDate("order.date_of_trip")).thenReturn(Date.valueOf(LocalDate.of(2021, 12, 10)));

            assertEquals(orderDao.extractResult(resultSet), order);
        } catch (SQLException ignored) {
        }
    }

}