package com.sidorovich.pavel.buber.api.util;

public interface Extractor<R, T> {

    R extract(T object);

}
