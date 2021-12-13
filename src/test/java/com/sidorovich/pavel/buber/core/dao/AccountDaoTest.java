package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Role;
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

public class AccountDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private AccountDao accountDao = new AccountDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO: 12/13/2021
    @Test
    public void readAccountByPhone() {
//        QueryGenerator queryGenerator = Mockito.mock(QueryGenerator.class);
//
//        when(queryGeneratorFactory.of(connectionPool)).thenReturn(queryGenerator);
//        when(queryGenerator.fetch("+375 29 111-11-11")).thenReturn(Optional.empty());
//
//        assertEquals(mocked.readAccountByPhone("+375 29 111-11-11"), Optional.empty());
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(accountDao.getTableName().contains(".account"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "account.id", "account.phone",
                "account.password_hash",
                "account.role_name"
        );

        assertEquals(accountDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        Account account = new Account(4L, "+375 29 111-11-11", "12345", Role.CLIENT);

        columnsByValues.put("account.phone", account.getPhone());
        columnsByValues.put("account.password_hash", account.getPasswordHash());
        columnsByValues.put("account.role_name", account.getRole().name());

        assertEquals(accountDao.getColumnsAndValuesToBeInserted(account), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(accountDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        Account account = new Account(1L, "+375 29 111-11-11", "1234", Role.CLIENT);

        try {
            when(resultSet.getLong("account.id")).thenReturn(1L);
            when(resultSet.getString("account.phone")).thenReturn("+375 29 111-11-11");
            when(resultSet.getString("account.password_hash")).thenReturn("1234");
            when(resultSet.getString("account.role_name")).thenReturn("CLIENT");

            assertEquals(accountDao.extractResult(resultSet), account);
            verify(resultSet).getLong("account.id");
            verify(resultSet).getString("account.phone");
            verify(resultSet).getString("account.password_hash");
            verify(resultSet).getString("account.role_name");
        } catch (SQLException ignored) {
        }
    }

}