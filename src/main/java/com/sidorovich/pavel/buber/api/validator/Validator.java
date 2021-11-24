package com.sidorovich.pavel.buber.api.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Validator<T, R> {

    R validate(T object);

    default boolean isValid (Matcher matcher, Pattern pattern, String text){
        matcher.usePattern(pattern);
        matcher.reset(text);

        return matcher.matches();
    }

}
