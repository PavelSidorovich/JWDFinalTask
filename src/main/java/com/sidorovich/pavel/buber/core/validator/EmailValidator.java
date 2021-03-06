package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.BiValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements BiValidator<String, Boolean, Map<String, String>> {

    private static final String EMAIL_REQUIRED_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String EMAIL_OPTIONAL_REGEX = "^([\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4})?$";
    private static final String EMAIL_ATTR_PARAM_NAME = "email";
    private static final String INVALID_EMAIL_KEY = "msg.invalid.email";

    private EmailValidator() {
    }

    @Override
    public Map<String, String> validate(String email, Boolean isOptional, ResourceBundle resourceBundle) {
        final Pattern pattern = isOptional
                ? Pattern.compile(EMAIL_OPTIONAL_REGEX)
                : Pattern.compile(EMAIL_REQUIRED_REGEX);
        final Matcher matcher = pattern.matcher(email);

        return new HashMap<>(checkEmail(email, pattern, matcher, resourceBundle));
    }

    private Map<String, String> checkEmail(String email, Pattern pattern, Matcher matcher,
                                           ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (!isValid(matcher, pattern, email)) {
            errorsByMessages.put(EMAIL_ATTR_PARAM_NAME, resourceBundle.getString(INVALID_EMAIL_KEY));
        }

        return errorsByMessages;
    }

    public static EmailValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EmailValidator INSTANCE = new EmailValidator();
    }

}
