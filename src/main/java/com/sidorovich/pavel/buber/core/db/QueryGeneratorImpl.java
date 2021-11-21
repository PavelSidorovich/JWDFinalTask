package com.sidorovich.pavel.buber.core.db;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.db.ResultSetExtractor;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class QueryGeneratorImpl implements QueryGenerator {

    private static final Logger LOG = LogManager.getLogger(QueryGeneratorImpl.class);

    private static final String SELECT = "SELECT ";
    private static final String UPDATE = "UPDATE ";
    private static final String DELETE = "DELETE ";
    private static final String FROM = "FROM ";
    private static final String WHERE = "WHERE ";
    private static final String SET = "SET ";
    private static final String AND = "AND ";
    private static final String OR = "OR ";
    private static final String COMMA = ", ";
    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";
    private static final String OPEN_BRACE = "(";
    private static final String CLOSE_BRACE = ")";
    private static final String INNER_JOIN = "INNER JOIN ";
    private static final String ON = "ON ";
    private static final String EQUALS = "= ";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String DESC = "DESC ";
    private static final String CONDITION = "= ?";
    private static final String PARAMETER_TO_BE_INSERTED = "?";
    private static final String WHITE_SPACE = " ";
    private static final String COUNT = "COUNT(%s) ";

    private final ConnectionPool connectionPool;
    private final List<Object> preparedStatementValues = new ArrayList<>();

    private String sqlQuery = "";

    public QueryGeneratorImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public QueryGenerator select(Set<String> columns) {
        reset();
        StringBuilder sb = new StringBuilder(sqlQuery);

        sb.append(SELECT);
        columns.forEach(column -> {
            sb.append(column);
            sb.append(COMMA);
        });
        sb.deleteCharAt(sb.length() - 2); // delete ","
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public QueryGenerator insertInto(String table, Set<String> columns) {
        reset();
        StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(INSERT)
          .append(table)
          .append(WHITE_SPACE)
          .append(OPEN_BRACE);
        for (String column : columns) {
            sb.append(column)
              .append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1)
          .deleteCharAt(sb.length() - 1)
          .append(CLOSE_BRACE)
          .append(WHITE_SPACE);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public QueryGenerator update(String table) {
        reset();
        sqlQuery += UPDATE + table + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator delete() {
        reset();
        sqlQuery += DELETE;
        return this;
    }

    @Override
    public QueryGenerator values(Collection<Object> values) {
        StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(VALUES)
          .append(OPEN_BRACE);
        for (Object value : values) {
            preparedStatementValues.add(value);
            sb.append(PARAMETER_TO_BE_INSERTED)
              .append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1)
          .deleteCharAt(sb.length() - 1);
        sb.append(CLOSE_BRACE);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public QueryGenerator where(String column, Object value) {
        preparedStatementValues.add(value);
        sqlQuery += WHERE + column + CONDITION + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator and(String column, Object value) {
        preparedStatementValues.add(value);
        sqlQuery += AND + column + CONDITION + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator or(String column, Object value) {
        preparedStatementValues.add(value);
        sqlQuery += OR + column + CONDITION + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator from(String table) {
        sqlQuery += FROM;
        sqlQuery += table;
        sqlQuery += WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator set(Map<String, Object> columnsAndValues) {
        StringBuilder sb = new StringBuilder(sqlQuery);
        fillParameters(sb, SET, COMMA, columnsAndValues);
        if (columnsAndValues.size() > 1) {
            sb.deleteCharAt(sb.length() - 2); // delete ","
        }
        sb.append(WHITE_SPACE);
        sqlQuery = sb.toString();
        return this;
    }

    @Override
    public Long executeUpdate() throws SQLException {
        try (final Connection connection = connectionPool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sqlQuery.trim(), Statement.RETURN_GENERATED_KEYS)) {
            return getIndex(statement);
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sqlQuery);
            throw e;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection was interrupted");
            Thread.currentThread().interrupt();
        }
        return -1L;
    }

    private Long getIndex(PreparedStatement statement) throws SQLException {
        for (int i = 0; i < preparedStatementValues.size(); i++) {
            statement.setObject(i + 1, preparedStatementValues.get(i));
        }
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getLong(1);
        }
        return 0L;
    }

    @Override
    public <T> List<T> fetch(ResultSetExtractor<T> extractor) {
        try (final Connection connection = connectionPool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sqlQuery.trim())) {
            for (int i = 0; i < preparedStatementValues.size(); i++) {
                statement.setObject(i + 1, preparedStatementValues.get(i));
            }
            return extractor.extractAll(statement.executeQuery());
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sqlQuery);
        } catch (InterruptedException e) {
            LOG.warn("takeConnection was interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityExtractionFailedException e) {
            LOG.warn(e);
        }
        return Collections.emptyList();
    }

    @Override
    public QueryGenerator orderBy(String column) {
        sqlQuery += ORDER_BY + column + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator desc() {
        sqlQuery += DESC + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator innerJoin(String trgTable) {
        sqlQuery += INNER_JOIN + trgTable + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator on(String srcColumn, String trgColumn) {
        sqlQuery += ON + srcColumn + WHITE_SPACE
                    + EQUALS + trgColumn + WHITE_SPACE;
        return this;
    }

    @Override
    public QueryGenerator count(String column) {
        reset();
        sqlQuery += SELECT + String.format(COUNT, column);
        return this;
    }

    @Override
    public void reset() {
        sqlQuery = "";
        preparedStatementValues.clear();
    }

    private void fillParameters(StringBuilder sb, String sqlCommand,
                                String sqlSign, Map<String, Object> columnsAndValues) {
        sb.append(sqlCommand);
        for (String key : columnsAndValues.keySet()) {
            sb.append(key);
            sb.append(CONDITION);
            if (columnsAndValues.size() > 1) {
                sb.append(sqlSign);
            }
            preparedStatementValues.add(columnsAndValues.get(key));
        }
    }

}
