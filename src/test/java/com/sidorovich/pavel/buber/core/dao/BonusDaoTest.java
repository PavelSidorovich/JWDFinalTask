package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

public class BonusDaoTest {

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private QueryGeneratorFactory queryGeneratorFactory;

    @InjectMocks
    private BonusDao bonusDao = new BonusDao(connectionPool, queryGeneratorFactory);

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO: 12/13/2021
    @Test
    public void testFindBonusesByUserId() {
    }

    // TODO: 12/13/2021
    @Test
    public void testFindBonusesByUserIdAndDiscount() {
    }

    @Test
    public void getTableName_shouldReturnTableName_always() {
        assertTrue(bonusDao.getTableName().contains(".bonus"));
    }

    @Test
    public void getColumnNames_shouldReturnTableColumnNames_always() {
        List<String> columnNames = Arrays.asList(
                "bonus.id",
                "bonus.discount",
                "bonus.expires",
                "bonus.client_id"
        );

        assertEquals(bonusDao.getColumnNames(), columnNames);
    }

    @Test
    public void getColumnsAndValuesToBeInserted_shouldReturnColumnNamesAndValuesToBeInserted_always() {
        Map<String, Object> columnsByValues = new HashMap<>();
        Bonus bonus = new Bonus(
                4L,
                10d,
                Date.valueOf(LocalDate.of(2021, 12, 10)),
                BuberUser.with().account(new Account(1L, null, null, null)).build()
        );

        columnsByValues.put("bonus.client_id", bonus.getClient().getId().get());
        columnsByValues.put("bonus.discount", bonus.getDiscount());
        columnsByValues.put("bonus.expires", bonus.getExpireDate());

        assertEquals(bonusDao.getColumnsAndValuesToBeInserted(bonus), columnsByValues);
    }

    @Test
    public void getPrimaryColumnName_shouldReturnTablePrimaryKeyColumn_always() {
        assertTrue(bonusDao.getPrimaryColumnName().contains(".id"));
    }

    @Test
    public void extractResult_shouldExtractAccount_always() {
        ResultSet resultSet = mock(ResultSet.class);
        Bonus bonus = new Bonus(
                4L,
                10d,
                Date.valueOf(LocalDate.of(2021, 12, 10)),
                BuberUser.with().account(new Account(1L, null, null, null)).build()
        );

        try {
            when(resultSet.getLong("bonus.id")).thenReturn(4L);
            when(resultSet.getLong("bonus.client_id")).thenReturn(1L);
            when(resultSet.getDouble("bonus.discount")).thenReturn(10d);
            when(resultSet.getDate("bonus.expires")).thenReturn(Date.valueOf(LocalDate.of(2021, 12, 10)));

            assertEquals(bonusDao.extractResult(resultSet), bonus);
            verify(resultSet).getLong("bonus.id");
            verify(resultSet).getLong("bonus.client_id");
            verify(resultSet).getDouble("bonus.discount");
            verify(resultSet).getDate("bonus.expires");
        } catch (SQLException ignored) {
        }
    }

}