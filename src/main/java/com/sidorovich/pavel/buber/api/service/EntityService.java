package com.sidorovich.pavel.buber.api.service;

import com.sidorovich.pavel.buber.api.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.api.model.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity<T>> {

    T save(T entity) throws DuplicateKeyException;

    Optional<T> findById(Long id);

    List<T> findAll();

    T update(T entity) throws DuplicateKeyException;

    boolean delete(Long id);

}
