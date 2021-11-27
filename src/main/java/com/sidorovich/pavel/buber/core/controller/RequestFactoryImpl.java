package com.sidorovich.pavel.buber.core.controller;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestFactoryImpl implements RequestFactory {

    private RequestFactoryImpl() {
    }

    private static class Holder {
        private static final RequestFactoryImpl INSTANCE = new RequestFactoryImpl();
    }

    public static RequestFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<String, CommandResponse> forwardResponseCache = new ConcurrentHashMap<>();
    private final Map<String, CommandResponse> redirectResponseCache = new ConcurrentHashMap<>();

    @Override
    public CommandRequest createRequest(HttpServletRequest request) {
        return new PlainCommandRequest(request);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return forwardResponseCache.computeIfAbsent(path, PlainCommandResponse::new);
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return redirectResponseCache.computeIfAbsent(path, p -> new PlainCommandResponse(true, p));
    }

    @Override
    public <T> CommandResponse createRedirectJsonResponse(String command) {
        return new JsonResponse<>(
                true, command, null,
                JsonResponseStatus.SUCCESS, null
        );
    }

    @Override
    public <T> CommandResponse createJsonResponse(T object, JsonResponseStatus status, String msg) {
        return new JsonResponse<>(
                false, null, object, status, msg
        );
    }

    @Override
    public <T> CommandResponse createJsonResponse(T object, JsonResponseStatus status) {
        return new JsonResponse<>(
                false, null, object,
                status, null
        );
    }

}
