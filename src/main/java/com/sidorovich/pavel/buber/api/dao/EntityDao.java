package com.sidorovich.pavel.buber.api.dao;

import com.sidorovich.pavel.buber.api.model.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity<T>> {

    T save(T entity) throws SQLException;

    Optional<T> findById(Long id);

    List<T> findAll();

    T update(T entity);

    boolean delete(Long id) throws SQLException;

}
