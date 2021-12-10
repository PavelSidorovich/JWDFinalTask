package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.BiValidator;

import java.util.HashMap;
import java.util.Map;

public class PasswordValidator implements BiValidator<String, String, Map<String, String>> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,255}$";
    private static final String PASSWORDS_ARE_NOT_EQUAL_MSG = "Passwords are not equal";
    private static final String PASSWORD_MATCHING_ERROR_MSG =
            "Password should contain minimum eight characters, at least one letter and one number";
    private static final String INVALID_PASSWORD_MSG = "Valid password is required";
    private static final String PASSWORD_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAM_NAME = "passwordRepeat";

    private PasswordValidator(){
    }

    @Override
    public Map<String, String> validate(String password, String passwordRepeat) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (password != null) {
            if (!password.matches(PASSWORD_REGEX)) {
                errorsByMessages.put(PASSWORD_PARAM_NAME, PASSWORD_MATCHING_ERROR_MSG);
            } else if (!password.equals(passwordRepeat)) {
                errorsByMessages.put(PASSWORD_REPEAT_PARAM_NAME, PASSWORDS_ARE_NOT_EQUAL_MSG);
            }
        } else {
            errorsByMessages.put(PASSWORD_PARAM_NAME, INVALID_PASSWORD_MSG);
        }

        return errorsByMessages;
    }

    public static PasswordValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PasswordValidator INSTANCE = new PasswordValidator();
    }

}
