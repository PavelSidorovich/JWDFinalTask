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

public class EditPersonalInfoCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String LAST_NAME_REQUEST_PARAM_NAME = "lastName";
    private static final String FIRST_NAME_REQUEST_PARAM_NAME = "firstName";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";

    private final UserService userService;

    private EditPersonalInfoCommand(RequestFactory requestFactory,
                                    UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            user.ifPresent(buberUser -> updateUserPersonalInfo(request, buberUser));
        }

        return requestFactory.createRedirectResponse(PagePaths.ACCOUNT_CONTROL.getCommand());
    }

    private void updateUserPersonalInfo(CommandRequest request, BuberUser user) {
        BuberUser editedUser = BuberUser.with()
                                        .lastName(request.getParameter(LAST_NAME_REQUEST_PARAM_NAME))
                                        .firstName(request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME))
                                        .email(request.getParameter(EMAIL_REQUEST_PARAM_NAME))
                                        .cash(user.getCash())
                                        .status(user.getStatus())
                                        .account(user.getAccount())
                                        .build();

        userService.update(editedUser);
    }

    public static EditPersonalInfoCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EditPersonalInfoCommand INSTANCE = new EditPersonalInfoCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class));
    }

}
