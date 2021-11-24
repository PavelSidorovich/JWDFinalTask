package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

public class LogoutCommand extends CommonCommand {

    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";

    private LogoutCommand(RequestFactory requestFactory) {
        super(requestFactory);
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
            return requestFactory.createRedirectResponse(PagePaths.ERROR.getCommand());
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
