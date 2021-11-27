package com.sidorovich.pavel.buber.core.controller;

import java.util.Objects;

public class JsonResponse<T> extends PlainCommandResponse {

    private final T obj;
    private final JsonResponseStatus status;
    private final String message;

    public JsonResponse(boolean redirect, String command, T object,
                        JsonResponseStatus status, String message) {
        super(redirect, command);
        this.obj = object;
        this.status = status;
        this.message = message;
    }

    public T getObj() {
        return obj;
    }

    public JsonResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
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
