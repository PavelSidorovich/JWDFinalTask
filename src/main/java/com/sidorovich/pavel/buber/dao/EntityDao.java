package com.sidorovich.pavel.buber.dao;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T> {

    boolean create(T entity);

    Optional<T> read(Long id);

    List<T> readAll();

    boolean update(T entity);

    boolean delete(Long id);

}
