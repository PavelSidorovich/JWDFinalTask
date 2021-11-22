package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

public class LogoutCommand implements Command {

    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";

    private final RequestFactory requestFactory;

    private LogoutCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    private static class Holder {
        private static final LogoutCommand INSTANCE = new LogoutCommand(RequestFactoryImpl.getInstance());
    }

    public static LogoutCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (noLoggedInUserPresent(request)) {
            //todo: error - no user found cannot logout
            return null;
        }
        request.clearSession();
        return requestFactory.createRedirectResponse(PagePaths.MAIN.getCommand());
    }

    private boolean noLoggedInUserPresent(CommandRequest request) {
        return !request.sessionExists()
               || (request.sessionExists()
                   && !request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME)
                              .isPresent()
               );
    }

}
