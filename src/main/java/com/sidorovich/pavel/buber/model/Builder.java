package com.sidorovich.pavel.buber.model;

import java.util.Optional;

public interface Builder<T, R> {

//    T setId(Long id);

    T reset();

    Optional<R> getResult();

}
