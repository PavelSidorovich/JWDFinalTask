package com.sidorovich.pavel.buber.api.controller;

import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {

    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    <T> CommandResponse createRedirectJsonResponse(String command);

    <T> CommandResponse createJsonResponse(T object, JsonResponseStatus status, String msg);

    <T> CommandResponse createJsonResponse(T object, JsonResponseStatus status);

}
