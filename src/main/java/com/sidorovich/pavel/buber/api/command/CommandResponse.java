package com.sidorovich.pavel.buber.api.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
