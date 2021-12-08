package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StatisticsService {

    private static final Logger LOG = LogManager.getLogger(StatisticsService.class);

    private static final String PIE_CHART_SQL_QUERY =
            "SELECT count(status_name) as amount, status_name as status FROM buber.order group by status_name";
    private static final String LINE_CHART_SQL_QUERY =
            "SELECT count(date_of_trip) as amount, date_of_trip as tripDate FROM buber.order" +
            " where date_of_trip between now() - INTERVAL 7 DAY and now() group by date_of_trip";
    public static final String AMOUNT_COLUMN_NAME = "amount";
    public static final String STATUS_COLUMN_NAME = "status";
    public static final String TRIP_DATE_COLUMN_NAME = "tripDate";

    private final ConnectionPool connectionPool;

    private StatisticsService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Map<String, Integer> retrievePieChartData() throws SQLException {
        return executeQuery(PIE_CHART_SQL_QUERY, STATUS_COLUMN_NAME);
    }

    public Map<String, Integer> retrieveLineChartData() throws SQLException {
        return executeQuery(LINE_CHART_SQL_QUERY, TRIP_DATE_COLUMN_NAME);
    }

    private Map<String, Integer> executeQuery(String sql, String column) throws SQLException {
        try (final Connection connection = connectionPool.takeConnection();
             final Statement statement = connection.createStatement()) {
            return extractResult(statement.executeQuery(sql), column);
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sql);
            throw e;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection was interrupted");
            Thread.currentThread().interrupt();
        }

        return Collections.emptyMap();
    }

    private Map<String, Integer> extractResult(ResultSet resultSet, String column) throws SQLException {
        final Map<String, Integer> chartData = new HashMap<>();

        while (resultSet.next()) {
            chartData.put(
                    resultSet.getString(column),
                    resultSet.getInt(AMOUNT_COLUMN_NAME)
            );
        }

        return chartData;
    }

    public static StatisticsService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final StatisticsService INSTANCE =
                new StatisticsService(ConnectionPool.locking());
    }

}
