package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.AccountService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.Optional;

public class LoginCommand extends CommonCommand {

    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Invalid login or password";
    private static final String USER_BLOCKED_MSG = "User was blocked";

    private final AccountService accountService;
    private final UserService userService;

    private LoginCommand(RequestFactory requestFactory,
                         AccountService accountService, UserService userService) {
        super(requestFactory);
        this.accountService = accountService;
        this.userService = userService;
    }

    private static class Holder {
        private static final LoginCommand INSTANCE =
                new LoginCommand(
                        RequestFactoryImpl.getInstance(),
                        EntityServiceFactory.getInstance().serviceFor(AccountService.class),
                        EntityServiceFactory.getInstance().serviceFor(UserService.class)
                );
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

        final Account acc = account.get();
        final Optional<BuberUser> user = userService.findById(acc.getId().orElse(-1L));

        if (user.isPresent() && user.get().getStatus() == UserStatus.BLOCKED) {
            return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR, USER_BLOCKED_MSG);
        }

        return createUserSession(request, acc);
    }

    private CommandResponse createUserSession(CommandRequest request, Account acc) {
        String userPageCommand;

        request.clearSession();
        request.createSession();
        request.addToSession(USER_SESSION_ATTRIBUTE_NAME, acc);

        switch (acc.getRole()) {
        case ADMIN:
            userPageCommand = PagePaths.USER_CONTROL.getCommand();
            break;
        case DRIVER:
            userPageCommand = "";
            // TODO: 11/27/2021
            break;
        case CLIENT:
            userPageCommand = "";
            // TODO: 11/27/2021 show order page
            break;
        default:
            userPageCommand = PagePaths.ERROR.getCommand();
        }

        return requestFactory.createRedirectJsonResponse(userPageCommand);
    }

    private Optional<Account> findAccount(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);

        return accountService.authenticate(login, password);
    }

}
