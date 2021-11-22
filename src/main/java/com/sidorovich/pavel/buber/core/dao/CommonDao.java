package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.dao.EntityDao;
import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.model.Entity;
import com.sidorovich.pavel.buber.core.db.QueryGeneratorImpl;
import com.sidorovich.pavel.buber.exception.CannotFindEntityByIdException;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
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
    protected final ConnectionPool connectionPool;

    CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.connectionPool = connectionPool;
    }

    @Override
    public T save(T entity) throws SQLException {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        long id = queryGenerator.insertInto(getTableName(), getColumnsAndValuesToBeInserted(entity).keySet())
                                .values(getColumnsAndValuesToBeInserted(entity).values())
                                .executeUpdate();
        return entity.withId(id);
    }

    public T update(T entity) {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        try {
            queryGenerator.update(getTableName())
                          .set(getColumnsAndValuesToBeInserted(entity))
                          .where(getPrimaryColumnName(),
                                 entity.getId().orElseThrow(IdIsNotDefinedException::new))
                          .executeUpdate();
            return findById(entity.getId().orElseThrow(IdIsNotDefinedException::new))
                    .orElseThrow(CannotFindEntityByIdException::new);
        } catch (IdIsNotDefinedException e) {
            logger.warn("Entity id is not defined", e);
        } catch (Throwable throwable) {
            logger.warn("something went wrong", throwable);
        }
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        List<T> list = queryGenerator.select(getColumnNames())
                                     .from(getTableName())
                                     .where(getPrimaryColumnName(), String.valueOf(id))
                                     .fetch(this::extractResultCatchingException);
        return list.isEmpty()? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<T> findAll() {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .fetch(this::extractResultCatchingException);
    }

    @Override
    public boolean delete(Long id) {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        try {
            return queryGenerator.delete()
                                 .from(getTableName())
                                 .where(getPrimaryColumnName(), String.valueOf(id))
                                 .executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
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
