package com.sidorovich.pavel.buber.core.db;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.testng.Assert.*;

@ExtendWith(MockitoExtension.class)
public class QueryGeneratorImplTest {

    @Mock
    private ConnectionPool connectionPool;

    @InjectMocks
    private QueryGeneratorImpl queryGenerator;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        queryGenerator.reset();
    }

    @Test
    public void select_shouldAddSelectToQuery_always() {
        queryGenerator.select(new LinkedHashSet<>(Arrays.asList("column1", "column2", "column3")));

        assertEquals(queryGenerator.getSqlQuery(), "SELECT column1, column2, column3 ");
    }

    @Test
    public void insertInto_shouldAddInsertToQuery_always() {
        queryGenerator.insertInto("table", new LinkedHashSet<>(
                Arrays.asList("column1", "column2", "column3"))
        );

        assertEquals(queryGenerator.getSqlQuery(), "INSERT INTO table (column1, column2, column3) ");
    }

    @Test
    public void update_shouldAddUpdateToQuery_always() {
        queryGenerator.update("table");

        assertEquals(queryGenerator.getSqlQuery(), "UPDATE table ");
    }

    @Test
    public void delete_shouldAddUpdateToQuery_always() {
        queryGenerator.delete();

        assertEquals(queryGenerator.getSqlQuery(), "DELETE ");
    }

    @Test
    public void values_shouldAddValuesToQuery_always() {
        queryGenerator.values(Arrays.asList("value1", "value2", "value1"));

        assertEquals(queryGenerator.getSqlQuery(), "VALUES (?, ?, ?)");
    }

    @Test
    public void where_shouldAddWhereToQuery_always() {
        queryGenerator.where("column", "value");

        assertEquals(queryGenerator.getSqlQuery(), "WHERE column= ? ");
    }

    @Test
    public void and_shouldAddAndToQuery_always() {
        queryGenerator.and("column", "value");

        assertEquals(queryGenerator.getSqlQuery(), "AND column= ? ");
    }

    @Test
    public void or_shouldAddOrToQuery_always() {
        queryGenerator.or("column", "value");

        assertEquals(queryGenerator.getSqlQuery(), "OR column= ? ");
    }

    @Test
    public void from_shouldAddFromToQuery_always() {
        queryGenerator.from("table");

        assertEquals(queryGenerator.getSqlQuery(), "FROM table ");
    }

    @Test
    public void set_shouldAddSetToQuery_always() {
        Map<String, Object> columnsByValues = new LinkedHashMap<>();

        columnsByValues.put("column1", "value1");
        columnsByValues.put("column2", "value2");
        columnsByValues.put("column3", "value3");
        queryGenerator.set(columnsByValues);

        assertEquals(queryGenerator.getSqlQuery(), "SET column1= ?, column2= ?, column3= ? ");
    }

//    @Test
//    public void executeUpdate_shouldReturnIndexOfEntity_whenInsertQuery() {
//        Connection connection = mock(Connection.class);
//        PreparedStatement statement = mock(PreparedStatement.class);
//        ResultSet resultSet = mock(ResultSet.class);
//
//        try {
//            when(connectionPool.takeConnection()).thenReturn(connection);
//            when(connection.prepareStatement("", Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
//            when(statement.getGeneratedKeys()).thenReturn(resultSet);
//            when(statement.executeUpdate("")).thenReturn(0);
//            when(resultSet.next()).thenReturn(true);
//            when(resultSet.getLong(1)).thenReturn(1L);
//
//            assertSame(queryGenerator.executeUpdate(), 1L);
//        } catch (InterruptedException | SQLException ignored) {
//        }
//    }
//
//    @Test
//    public void testFetch() {
//    }

    @Test
    public void orderBy_shouldAddOrderByToQuery_always() {
        queryGenerator.orderBy("column");

        assertEquals(queryGenerator.getSqlQuery(), "ORDER BY column ");
    }

    @Test
    public void desc_shouldAddDescToQuery_always() {
        queryGenerator.desc();

        assertEquals(queryGenerator.getSqlQuery(), "DESC ");
    }

    @Test
    public void innerJoin_shouldAddInnerJoinToQuery_always() {
        queryGenerator.innerJoin("table");

        assertEquals(queryGenerator.getSqlQuery(), "INNER JOIN table ");
    }

    @Test
    public void on_shouldAddOnToQuery_always() {
        queryGenerator.on("srcColumn", "trgColumn");

        assertEquals(queryGenerator.getSqlQuery(), "ON srcColumn = trgColumn ");
    }

    @Test
    public void count() {
        queryGenerator.count("column");

        assertEquals(queryGenerator.getSqlQuery(), "SELECT COUNT(column) ");
    }

    @Test
    public void reset() {
        queryGenerator.reset();

        assertEquals(queryGenerator.getSqlQuery(), "");
    }

}