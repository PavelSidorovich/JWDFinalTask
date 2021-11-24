package com.sidorovich.pavel.buber.api.controller;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {

    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    <T> CommandResponse createJsonResponse(String command, boolean isRedirect, T object);

    <T> CommandResponse createJsonResponse(String command, boolean isRedirect);

}
