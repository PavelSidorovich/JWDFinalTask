package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.db.StatementExecutor;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    StatementExecutor SQL_EXECUTOR = StatementExecutor.getInstance();

    boolean create(T entity) throws InterruptedException;

    List<T> readAll() throws InterruptedException;

    Optional<T> read(Long id) throws InterruptedException;

    // FIXME: 10/18/2021 move to specification
    Optional<T> read(String uniqueProperty) throws InterruptedException;

    boolean update(Long id, T entity) throws InterruptedException;

    boolean delete(Long id) throws InterruptedException;

    boolean delete(T entity) throws InterruptedException;

}
