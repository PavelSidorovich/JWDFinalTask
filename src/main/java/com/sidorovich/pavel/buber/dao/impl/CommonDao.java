package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.dao.EntityDao;
import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.db.QueryGenerator;
import com.sidorovich.pavel.buber.db.impl.QueryGeneratorImpl;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class CommonDao<T extends Entity<T>> implements EntityDao<T> {

    protected static final String DATABASE_NAME = "buber";

    private final Logger logger;
    protected final QueryGenerator queryGenerator;

    public CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.queryGenerator = new QueryGeneratorImpl(connectionPool);
    }

    @Override
    public T create(T entity) {
        long id = queryGenerator.insertInto(getTableName(), getColumnsAndValuesToBeInserted(entity).keySet())
                                .values(getColumnsAndValuesToBeInserted(entity).values())
                                .executeUpdate();
        return entity.withId(id);
    }

    public T update(T entity) {
        try {
            long id = queryGenerator.update(getTableName())
                                    .set(getColumnsAndValuesToBeInserted(entity))
                                    .where(getPrimaryColumnName(),
                                           entity.getId().orElseThrow(IdIsNotDefinedException::new))
                                    .executeUpdate();
            return entity.withId(id);
        } catch (IdIsNotDefinedException e) {
            logger.warn("Entity id is not defined", e);
        } catch (Throwable throwable) {
            logger.warn("something went wrong", throwable);
        }
        return entity;
    }

    @Override
    public Optional<T> read(Long id) {
        List<T> list = queryGenerator.select(getColumnNames())
                                     .from(getTableName())
                                     .where(getPrimaryColumnName(), String.valueOf(id))
                                     .fetch(this::extractResultCatchingException);
        return list.isEmpty()? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<T> readAll() {
        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .fetch(this::extractResultCatchingException);
    }

    @Override
    public boolean delete(Long id) {
        return queryGenerator.delete()
                             .from(getTableName())
                             .where(getPrimaryColumnName(), String.valueOf(id))
                             .executeUpdate() == 1;
    }

    protected T extractResultCatchingException(ResultSet rs) throws EntityExtractionFailedException {
        try {
            return extractResult(rs);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("could not extract entity", e);
        }
    }

    protected abstract String getTableName();

    protected abstract Set<String> getColumnNames();

    protected abstract Map<String, Object> getColumnsAndValuesToBeInserted(T entity);

    protected abstract String getPrimaryColumnName();

    protected abstract T extractResult(ResultSet rs) throws SQLException;
}
