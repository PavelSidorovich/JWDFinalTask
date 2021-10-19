package com.sidorovich.pavel.buber.model;

import java.util.Optional;

public interface User<R> {

    R withId(Long id);

    Optional<Long> getId();

}
