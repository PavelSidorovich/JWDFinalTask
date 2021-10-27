package com.sidorovich.pavel.buber.model;

import java.util.Optional;

public interface Entity<T> {

    Optional<Long> getId();

    T withId(Long id);

}
