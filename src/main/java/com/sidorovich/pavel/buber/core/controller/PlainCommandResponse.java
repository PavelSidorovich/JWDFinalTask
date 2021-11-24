package com.sidorovich.pavel.buber.core.controller;

import com.sidorovich.pavel.buber.api.controller.CommandResponse;

import java.util.Objects;

public class PlainCommandResponse implements CommandResponse {

    private final boolean isRedirect;
    private final String path;

    public PlainCommandResponse(String path) {
        this(false, path);
    }

    public PlainCommandResponse(boolean isRedirect, String path) {
        this.isRedirect = isRedirect;
        this.path = path;
    }

    @Override
    public boolean isRedirect() {
        return isRedirect;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlainCommandResponse that = (PlainCommandResponse) o;
        return isRedirect == that.isRedirect && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isRedirect, path);
    }

    @Override
    public String toString() {
        return "PlainCommandResponse{" +
               "redirect=" + isRedirect +
               ", path='" + path + '\'' +
               '}';
    }

}
