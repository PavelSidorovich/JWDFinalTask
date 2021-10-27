package com.sidorovich.pavel.buber.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
