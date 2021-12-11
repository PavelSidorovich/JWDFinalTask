package com.sidorovich.pavel.buber.api.validator;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface BiValidator<T, P, R> {

    R validate(T object1, P object2, ResourceBundle resourceBundle);

    default boolean isValid(Matcher matcher, Pattern pattern, String text) {
        matcher.usePattern(pattern);
        matcher.reset(text);

        return matcher.matches();
    }

}
