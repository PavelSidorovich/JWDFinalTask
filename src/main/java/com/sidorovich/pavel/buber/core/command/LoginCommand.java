package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.AccountService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

public class LoginCommand extends CommonCommand {

    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Invalid login or password";

    private final AccountService accountService;

    private LoginCommand(RequestFactory requestFactory,
                        AccountService accountService) {
        super(requestFactory);
        this.accountService = accountService;
    }

    private static class Holder {
        private static final LoginCommand INSTANCE =
                new LoginCommand(RequestFactoryImpl.getInstance(),
                                 EntityServiceFactory.getInstance().serviceFor(AccountService.class));

    }

    public static LoginCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME).isPresent()) {
            request.clearSession(); // terminate current session
        }
        final Optional<Account> account = findAccount(request);

        if (!account.isPresent()) {
            return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR, ERROR_LOGIN_PASS_MESSAGE);
        }
        Account acc = account.get();

        request.clearSession();
        request.createSession();
        request.addToSession(USER_SESSION_ATTRIBUTE_NAME, acc);
        if (account.get().getRole() == Role.ADMIN) {
            return requestFactory.createRedirectJsonResponse(PagePaths.USER_CONTROL_PAGE.getCommand());
        }

        return null;
//        return null requestFactory.createRedirectResponse(PagePaths.ADMIN_PAGE.getCommand()); // TODO: 11/22/2021 send to users pages
    }

    private Optional<Account> findAccount(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);

        return accountService.authenticate(login, password);
    }

}
