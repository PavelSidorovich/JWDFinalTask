package com.sidorovich.pavel.buber.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    void create(T entity);

    List<T> readAll();

    Optional<T> read(Long id);

    boolean update(Long id, T entity);

    boolean delete(Long id);

    boolean delete(T entity);

}
