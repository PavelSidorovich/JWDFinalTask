package com.sidorovich.pavel.buber.model;

import java.util.Optional;

public interface Builder<R> {

    void setId(Integer id);

    void reset();

    Optional<R> getResult();

}
