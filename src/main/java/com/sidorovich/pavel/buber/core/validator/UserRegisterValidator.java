package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.validator.BiValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegisterValidator implements BiValidator<BuberUser, String, Map<String, String>> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final String PHONE_REGEX = "[+]375[ ]\\d{2}[ ]\\d{3}[-]\\d{2}[-]\\d{2}";
    private static final String NAME_REGEX = "[a-zA-Zа-яА-Я]{2,}";
    private static final String INCORRECT_FIRST_NAME_MSG = "Valid first name is required";
    private static final String INCORRECT_LAST_NAME_MSG = "Valid last name is required";
    private static final String INCORRECT_PHONE_MSG = "Valid phone is required";
    private static final String PASSWORDS_ARE_NOT_EQUAL_MSG = "Passwords are not equal";
    private static final String PASSWORD_MATCHING_ERROR_MSG =
            "Password should contain minimum eight characters, at least one letter and one number";
    private static final String INVALID_PASSWORD_MSG = "Valid password is required";
    private static final String F_NAME_PARAM_NAME = "fName";
    private static final String L_NAME_PARAM_NAME = "lName";
    private static final String PHONE_PARAM_NAME = "phone";
    private static final String PASSWORD_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAM_NAME = "passwordRepeat";

    private UserRegisterValidator() {
    }

    private static class Holder {
        private static final UserRegisterValidator INSTANCE = new UserRegisterValidator();
    }

    public static UserRegisterValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(BuberUser user, String passwordRepeat) {
        Map<String, String> errorsByMessages = new HashMap<>();
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(user.getFirstName());

        errorsByMessages.putAll(checkPersonalInfo(user, pattern, matcher));
        errorsByMessages.putAll(checkPasswords(user.getPasswordHash(), passwordRepeat));

        return errorsByMessages;
    }

    private Map<String, String> checkPersonalInfo(BuberUser user, Pattern pattern, Matcher matcher) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (!isValid(matcher, pattern, user.getFirstName())) {
            errorsByMessages.put(F_NAME_PARAM_NAME, INCORRECT_FIRST_NAME_MSG);
        }
        if (!isValid(matcher, pattern, user.getLastName())) {
            errorsByMessages.put(L_NAME_PARAM_NAME, INCORRECT_LAST_NAME_MSG);
        }
        if (!isValid(matcher, Pattern.compile(PHONE_REGEX), user.getPhone())) {
            errorsByMessages.put(PHONE_PARAM_NAME, INCORRECT_PHONE_MSG);
        }

        return errorsByMessages;
    }

    private Map<String, String> checkPasswords(String password, String passwordRepeat) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (password != null) {
            if (password.matches(PASSWORD_REGEX)) {
                errorsByMessages.put(PASSWORD_PARAM_NAME, PASSWORD_MATCHING_ERROR_MSG);
            } else if (!password.equals(passwordRepeat)) {
                errorsByMessages.put(PASSWORD_REPEAT_PARAM_NAME, PASSWORDS_ARE_NOT_EQUAL_MSG);
            }
        } else {
            errorsByMessages.put(PASSWORD_PARAM_NAME, INVALID_PASSWORD_MSG);
        }

        return errorsByMessages;
    }

}
