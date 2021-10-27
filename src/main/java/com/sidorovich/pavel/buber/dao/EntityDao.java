package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.model.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity<T>> {

    T create(T entity);

    Optional<T> read(Long id);

    List<T> readAll();

    T update(T entity);

    boolean delete(Long id);

}
