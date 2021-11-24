package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.validator.UserRegisterValidator;

import java.math.BigDecimal;

public class UserRegisterCommand extends CommonCommand {

    private static final String F_NAME_REQUEST_PARAM_NAME = "fName";
    private static final String L_NAME_REQUEST_PARAM_NAME = "lName";
    private static final String PHONE_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_REQUEST_PARAM_NAME = "passwordRepeat";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String PASSWORDS_ARE_NOT_EQUAL_MSG = "Passwords are not equal";
    private static final String TOO_FEW_CHARACTERS_MSG = "Password should contain at least 8 characters";
    
    private final Validator<BuberUser, String> validator;

    private UserRegisterCommand(RequestFactory requestFactory,
                                Validator<BuberUser, String> validator) {
        super(requestFactory);
        this.validator = validator;
    }

    private static class Holder {
        private static final UserRegisterCommand INSTANCE =
                new UserRegisterCommand(
                        RequestFactoryImpl.getInstance(),
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

        if (password != null && !password.equals(passwordRepeat)) {
            return requestFactory.createJsonResponse(
                    PagePaths.REGISTER.getCommand(), false, PASSWORDS_ARE_NOT_EQUAL_MSG
            );
        } else if (password != null && password.length() < 8) {
            return requestFactory.createJsonResponse(
                    PagePaths.REGISTER.getCommand(), false, TOO_FEW_CHARACTERS_MSG
            );
        }

        return processRegisterRequest(fName, lName, phone, password, email);
    }

    private CommandResponse processRegisterRequest(String fName, String lName, String phone, String password,
                                                   String email) {
        BuberUser userToRegister = buildUser(fName, lName, phone, password, email);
        String registerMsg = validator.validate(userToRegister);

        return registerMsg == null
                ? requestFactory.createJsonResponse(PagePaths.LOGIN.getCommand(), true)
                : requestFactory.createJsonResponse(PagePaths.REGISTER.getCommand(), false, registerMsg);
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
