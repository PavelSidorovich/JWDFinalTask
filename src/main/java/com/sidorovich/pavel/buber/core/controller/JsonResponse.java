package com.sidorovich.pavel.buber.core.controller;

import java.util.Objects;

public class JsonResponse<T> extends PlainCommandResponse {

    private final T obj;

    public JsonResponse(boolean redirect, String command, T object) {
        super(redirect, command);
        this.obj = object;
    }

    public T getObj() {
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        JsonResponse<?> that = (JsonResponse<?>) o;
        return Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), obj);
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
               "object=" + obj +
               ", redirect=" + isRedirect() +
               ", path='" + getPath() + '\'' +
               '}';
    }

}
