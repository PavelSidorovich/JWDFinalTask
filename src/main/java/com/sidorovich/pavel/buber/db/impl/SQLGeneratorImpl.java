package com.sidorovich.pavel.buber.db.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.db.ResultSetExtractor;
import com.sidorovich.pavel.buber.db.SQLGenerator;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SQLGeneratorImpl implements SQLGenerator {

    private static final Logger LOG = LogManager.getLogger(SQLGeneratorImpl.class);

    private static final String SELECT_SQL = "SELECT ";
    private static final String UPDATE_SQL = "UPDATE ";
    private static final String DELETE_SQL = "DELETE ";
    private static final String FROM_SQL = "FROM ";
    private static final String WHERE_SQL = "WHERE ";
    private static final String SET_SQL = "SET ";
    private static final String AND_SQL = "AND ";
    private static final String OR_SQL = "OR ";
    private static final String COMMA_SQL = ", ";
    private static final String INSERT_SQL = "INSERT INTO ";
    private static final String VALUES_SQL = "VALUES ";
    private static final String OPEN_BRACE = "(";
    private static final String CLOSE_BRACE = ")";
    private static final String INNER_JOIN_SQL = "INNER JOIN ";
    private static final String ON_SQL = "ON ";
    private static final String EQUALS_SQL = "= ";
    private static final String ORDER_BY_SQL = "ORDER BY ";
    private static final String DESC_SQL = "DESC ";
    private static final String CONDITION_SQL = "= ?";
    private static final String PARAMETER_TO_BE_INSERTED_SQL = "?";
    private static final String WHITE_SPACE_SQL = " ";

    private final ConnectionPool connectionPool;
    private String sqlQuery = "";
    private final List<String> preparedStatementValues = new ArrayList<>();

    public SQLGeneratorImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public SQLGenerator select(String... columns) {
        reset();
        StringBuilder sb = new StringBuilder(sqlQuery);

        sb.append(SELECT_SQL);
        Arrays.stream(columns).forEach(column -> {
            sb.append(column);
            sb.append(COMMA_SQL);
        });
        sb.deleteCharAt(sb.length() - 2); // delete ","
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public SQLGenerator insertInto(String table, String... columns) {
        reset();
        StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(INSERT_SQL)
          .append(table)
          .append(WHITE_SPACE_SQL)
          .append(OPEN_BRACE);
        for (String column : columns) {
            sb.append(column)
              .append(COMMA_SQL);
        }
        sb.deleteCharAt(sb.length() - 1)
          .deleteCharAt(sb.length() - 1)
          .append(CLOSE_BRACE)
          .append(WHITE_SPACE_SQL);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public SQLGenerator update(String table) {
        reset();
        sqlQuery += UPDATE_SQL + table + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator delete() {
        reset();
        sqlQuery += DELETE_SQL;
        return this;
    }

    @Override
    public SQLGenerator values(String... values) {
        StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(VALUES_SQL)
          .append(OPEN_BRACE);
        for (String value : values) {
            preparedStatementValues.add(value);
            sb.append(PARAMETER_TO_BE_INSERTED_SQL)
              .append(COMMA_SQL);
        }
        sb.deleteCharAt(sb.length() - 1)
          .deleteCharAt(sb.length() - 1);
        sb.append(CLOSE_BRACE);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public SQLGenerator where(String column, String value) {
        preparedStatementValues.add(value);
        sqlQuery += WHERE_SQL + column + CONDITION_SQL + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator and(String column, String value) {
        preparedStatementValues.add(value);
        sqlQuery += AND_SQL + column + CONDITION_SQL + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator or(String column, String value) {
        preparedStatementValues.add(value);
        sqlQuery += OR_SQL + column + CONDITION_SQL + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator from(String table) {
        sqlQuery += FROM_SQL;
        sqlQuery += table;
        sqlQuery += WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator set(String... values) {
        StringBuilder sb = new StringBuilder(sqlQuery);
        fillParameters(sb, SET_SQL, COMMA_SQL, values);
        if (values.length > 2) {
            sb.deleteCharAt(sb.length() - 2); // delete ","
        }
        sb.append(WHITE_SPACE_SQL);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public int executeUpdate() throws InterruptedException {
        try (final Connection connection = connectionPool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sqlQuery.trim())) {
            for (int i = 0; i < preparedStatementValues.size(); i++) {
                statement.setString(i + 1, preparedStatementValues.get(i));
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sqlQuery);
        } catch (InterruptedException e) {
            LOG.warn("Fail on executing query", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return 0;
    }

    @Override
    public <T> List<T> fetch(ResultSetExtractor<T> extractor) throws InterruptedException {
        try (final Connection connection = connectionPool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sqlQuery.trim())) {
            for (int i = 0; i < preparedStatementValues.size(); i++) {
                statement.setString(i + 1, preparedStatementValues.get(i));
            }
            return extractor.extractAll(statement.executeQuery());
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sqlQuery);
        } catch (EntityExtractionFailedException e) {
            LOG.error("Could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.warn("Fail on executing query", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    @Override
    public SQLGenerator orderBy(String column) {
        sqlQuery += ORDER_BY_SQL + column + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator desc() {
        sqlQuery += DESC_SQL + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator innerJoin(String trgTable) {
        sqlQuery += INNER_JOIN_SQL + trgTable + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public SQLGenerator on(String srcColumn, String trgColumn) {
        sqlQuery += ON_SQL + srcColumn + WHITE_SPACE_SQL
                    + EQUALS_SQL + trgColumn + WHITE_SPACE_SQL;
        return this;
    }

    @Override
    public void reset() {
        sqlQuery = "";
        preparedStatementValues.clear();
    }

    private void fillParameters(StringBuilder sb, String sqlCommand,
                                String sqlSign, String[] conditions) {
        sb.append(sqlCommand);
        for (int i = 0; i < conditions.length; i += 2) {
            sb.append(conditions[i]);
            sb.append(CONDITION_SQL);
            if (conditions.length > 2) {
                sb.append(sqlSign);
            }
            preparedStatementValues.add(conditions[i + 1]);
        }
    }

}
