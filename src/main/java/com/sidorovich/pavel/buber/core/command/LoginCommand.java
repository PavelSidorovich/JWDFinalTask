package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.AccountService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

public class LoginCommand implements Command {

    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Invalid login or password";

    private final RequestFactory requestFactory;
    private final AccountService accountService;

    private LoginCommand(RequestFactory requestFactory,
                        AccountService accountService) {
        this.requestFactory = requestFactory;
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
            //todo: error - user already logged in
            return null;
        }
        final Optional<Account> account = findAccount(request);

        if (!account.isPresent()) {
            request.addAttributeToJsp(ERROR_LOGIN_PASS_ATTRIBUTE, ERROR_LOGIN_PASS_MESSAGE);
            return requestFactory.createForwardResponse(PagePaths.LOGIN.getPath());
        }
        request.clearSession();
        request.createSession();
        request.addToSession(USER_SESSION_ATTRIBUTE_NAME, account.get());

        return requestFactory.createRedirectResponse(PagePaths.MAIN.getCommand()); // TODO: 11/22/2021 send to users pages
    }

    private Optional<Account> findAccount(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);

        return accountService.authenticate(login, password);
    }

}
