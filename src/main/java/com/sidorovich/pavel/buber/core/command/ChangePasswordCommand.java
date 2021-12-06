package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.Optional;

public class ChangePasswordCommand extends CommonCommand {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_REQUEST_PARAM_NAME = "passwordRepeat";

    private final UserService userService;

    private ChangePasswordCommand(RequestFactory requestFactory,
                                  UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            user.ifPresent(buberUser -> changePassword(request, buberUser));
        }

        return requestFactory.createRedirectResponse(PagePaths.ACCOUNT_CONTROL.getCommand());
    }

    private void changePassword(CommandRequest request, BuberUser user) {
        String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        String passwordRepeat = request.getParameter(PASSWORD_REPEAT_REQUEST_PARAM_NAME);

        if (password.matches(PASSWORD_REGEX) && password.equals(passwordRepeat)) {
            BuberUser editedUser = user.withAccount(user.getAccount().withPasswordHash(password));

            userService.updatePassword(editedUser);
        }
    }

    public static ChangePasswordCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class));
    }

}
