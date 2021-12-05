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

public class AccountControlCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";

    private final UserService userService;

    private AccountControlCommand(RequestFactory requestFactory,
                                UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            user.ifPresent(buberUser -> request.addAttributeToJsp(USER_SESSION_PARAM_NAME, buberUser));
        }

        return requestFactory.createForwardResponse(PagePaths.ACCOUNT_CONTROL.getJspPath());
    }

    public static AccountControlCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AccountControlCommand INSTANCE = new AccountControlCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class));
    }

}
