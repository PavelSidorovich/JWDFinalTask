package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.BiValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PasswordValidator implements BiValidator<String, String, Map<String, String>> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,255}$";
    private static final String PASSWORD_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAM_NAME = "passwordRepeat";
    private static final String INVALID_PASSWORD_KEY = "msg.invalid.password";
    private static final String INVALID_REPEAT_PASSWORD_KEY = "msg.invalid.repeatPassword";
    private static final String INVALID_GENERAL_PASSWORD_KEY = "msg.invalid.general.password";

    private PasswordValidator() {
    }

    @Override
    public Map<String, String> validate(String password, String passwordRepeat, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (password != null) {
            if (!password.matches(PASSWORD_REGEX)) {
                errorsByMessages.put(PASSWORD_PARAM_NAME, resourceBundle.getString(INVALID_PASSWORD_KEY));
            } else if (!password.equals(passwordRepeat)) {
                errorsByMessages.put(PASSWORD_REPEAT_PARAM_NAME, resourceBundle.getString(INVALID_REPEAT_PASSWORD_KEY));
            }
        } else {
            errorsByMessages.put(PASSWORD_PARAM_NAME, resourceBundle.getString(INVALID_GENERAL_PASSWORD_KEY));
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
