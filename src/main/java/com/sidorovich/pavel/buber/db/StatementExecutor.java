package com.sidorovich.pavel.buber.db;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class StatementExecutor implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(StatementExecutor.class);

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.locking();

    private StatementExecutor() {
        CONNECTION_POOL.init();
    }

    private static class InstanceCreator {

        static StatementExecutor INSTANCE = new StatementExecutor();
    }

    public static StatementExecutor getInstance() {
        return StatementExecutor.InstanceCreator.INSTANCE;
    }

    @Override
    public void close() {
        CONNECTION_POOL.shutDown();
    }

    public int executeUpdate(String sql) throws InterruptedException {
        try (final Connection connection = CONNECTION_POOL.takeConnection();
             final Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sql);
        } catch (InterruptedException e) {
            LOG.warn("Thread interrupted while taking connection", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return 0;
    }

    public <T> List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        try (final Connection connection = CONNECTION_POOL.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            LOG.error("Could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.warn("Thread interrupted while taking connection", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    private static <T> List<T> executePrepared(String sql, ResultSetExtractor<T> extractor,
                                               StatementPreparator statementPreparation) throws InterruptedException {
        try (final Connection connection = CONNECTION_POOL.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            LOG.error("Sql exception occurred", e);
            LOG.debug("Sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            LOG.error("Could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.warn("Thread interrupted while taking connection", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }
}
