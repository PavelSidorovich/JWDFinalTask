package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class UserDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private UserDao userDao = new UserDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(userDao.getTableName().contains(".buber_user"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "buber_user.user_id",
                "buber_user.first_name",
                "buber_user.last_name",
                "buber_user.email",
                "buber_user.money",
                "buber_user.status_name"
        );

        assertEquals(userDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        BuberUser user = BuberUser.with()
                                  .account(new Account(1L, null, null, null))
                                  .cash(BigDecimal.TEN)
                                  .firstName("Pavel")
                                  .lastName("Sidorovich")
                                  .email("pavsid@mail.ru")
                                  .status(UserStatus.BLOCKED)
                                  .build();

        columnsByValues.put("buber_user.user_id", user.getId().get());
        columnsByValues.put("buber_user.first_name", user.getFirstName());
        columnsByValues.put("buber_user.last_name", user.getLastName());
        columnsByValues.put("buber_user.email", user.getEmail().get());
        columnsByValues.put("buber_user.money", user.getCash());
        columnsByValues.put("buber_user.status_name", user.getStatus().name());

        assertEquals(userDao.getColumnsAndValuesToBeInserted(user), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(userDao.getPrimaryColumnName().contains(".user_id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        BuberUser user = BuberUser.with()
                                  .account(new Account(1L, null, null, null))
                                  .cash(BigDecimal.TEN)
                                  .firstName("Pavel")
                                  .lastName("Sidorovich")
                                  .email("pavsid@mail.ru")
                                  .status(UserStatus.BLOCKED)
                                  .build();

        try {
            when(resultSet.getLong("buber_user.user_id")).thenReturn(1L);
            when(resultSet.getString("buber_user.first_name")).thenReturn("Pavel");
            when(resultSet.getString("buber_user.last_name")).thenReturn("Sidorovich");
            when(resultSet.getString("buber_user.email")).thenReturn("pavsid@mail.ru");
            when(resultSet.getBigDecimal("buber_user.money")).thenReturn(BigDecimal.TEN);
            when(resultSet.getString("buber_user.status_name")).thenReturn("BLOCKED");

            assertEquals(userDao.extractResult(resultSet), user);
        } catch (SQLException ignored) {
        }
    }

}