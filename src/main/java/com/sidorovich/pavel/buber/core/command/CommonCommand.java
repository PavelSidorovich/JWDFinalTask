package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;

public abstract class CommonCommand implements Command {

    protected final RequestFactory requestFactory;

    public CommonCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

}
