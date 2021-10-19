package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.dao.EntityDao;
import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.db.SQLGenerator;
import com.sidorovich.pavel.buber.db.impl.SQLGeneratorImpl;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class CommonDao<T> implements EntityDao<T> {

    protected static final String DATABASE_NAME = "buber";

    private final Logger logger;
    protected final SQLGenerator sqlGenerator;

    public CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.sqlGenerator = new SQLGeneratorImpl(connectionPool);
    }

    @Override
    public Optional<T> read(Long id) {
        try {
            List<T> list = sqlGenerator.select(getColumnNames())
                                       .from(getTableName())
                                       .where(getPrimaryColumnName(), String.valueOf(id))
                                       .fetch(this::extractResultCatchingException);
            return list.isEmpty()? Optional.empty() : Optional.of(list.get(0));
        } catch (InterruptedException e) {
            logger.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<T> readAll() {
        try {
            return sqlGenerator.select(getColumnNames())
                               .from(getTableName())
                               .fetch(this::extractResultCatchingException);
        } catch (InterruptedException e) {
            logger.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            return sqlGenerator.delete()
                               .from(getTableName())
                               .where(getPrimaryColumnName(), String.valueOf(id))
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            logger.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    protected abstract String getTableName();

    protected abstract String[] getColumnNames();

    protected abstract String getPrimaryColumnName();

    protected abstract T extractResult(ResultSet rs) throws SQLException;

    protected T extractResultCatchingException(ResultSet rs) throws EntityExtractionFailedException {
        try {
            return extractResult(rs);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("could not extract entity", e);
        }
    }
}
