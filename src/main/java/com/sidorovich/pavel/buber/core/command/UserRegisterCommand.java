package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.validator.UserRegisterValidator;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.Map;

public class UserRegisterCommand extends CommonCommand {

    private static final String F_NAME_REQUEST_PARAM_NAME = "fName";
    private static final String L_NAME_REQUEST_PARAM_NAME = "lName";
    private static final String PHONE_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_REQUEST_PARAM_NAME = "passwordRepeat";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";

    private final UserService userService;
    private final BiValidator<BuberUser, String, Map<String, String>> validator;

    private UserRegisterCommand(RequestFactory requestFactory,
                                UserService userService,
                                BiValidator<BuberUser, String, Map<String, String>> validator) {
        super(requestFactory);
        this.userService = userService;
        this.validator = validator;
    }

    private static class Holder {
        private static final UserRegisterCommand INSTANCE =
                new UserRegisterCommand(
                        RequestFactoryImpl.getInstance(),
                        EntityServiceFactory.getInstance().serviceFor(UserService.class),
                        UserRegisterValidator.getInstance()
                );
    }

    public static UserRegisterCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String fName = request.getParameter(F_NAME_REQUEST_PARAM_NAME);
        String lName = request.getParameter(L_NAME_REQUEST_PARAM_NAME);
        String phone = request.getParameter(PHONE_REQUEST_PARAM_NAME);
        String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        String passwordRepeat = request.getParameter(PASSWORD_REPEAT_REQUEST_PARAM_NAME);
        String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);

        BuberUser buberUser = buildUser(fName, lName, phone, password, email);

        return processRegisterRequest(buberUser, passwordRepeat);
    }

    private CommandResponse processRegisterRequest(BuberUser user, String passwordRepeat) {
        Map<String, String> errorsByMessages = validator.validate(user, passwordRepeat);

        if (errorsByMessages.isEmpty()) {
            try {
                userService.save(user);

                return requestFactory.createRedirectJsonResponse(PagePaths.LOGIN.getCommand());
            } catch (DuplicateKeyException e) {
                errorsByMessages.put(e.getAttribute(), e.getMessage());
                return requestFactory.createJsonResponse(errorsByMessages, JsonResponseStatus.ERROR, null);
            }
        }

        return requestFactory.createJsonResponse(errorsByMessages, JsonResponseStatus.ERROR, null);
    }

    private BuberUser buildUser(String fName, String lName, String phone, String password, String email) {
        return BuberUser.with()
                        .account(new Account(phone, password, Role.CLIENT))
                        .status(UserStatus.ACTIVE)
                        .cash(BigDecimal.ZERO)
                        .email(email)
                        .firstName(fName)
                        .lastName(lName)
                        .build();
    }

}
