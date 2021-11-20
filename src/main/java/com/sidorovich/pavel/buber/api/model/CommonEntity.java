package com.sidorovich.pavel.buber.api.model;

import java.util.Optional;

public abstract class CommonEntity<T> implements Entity<T> {

    protected final Long id;

    public CommonEntity(Long id) {
        this.id = id;
    }

    @Override
    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
}
