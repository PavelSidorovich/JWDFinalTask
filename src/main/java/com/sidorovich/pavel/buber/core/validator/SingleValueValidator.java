package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleValueValidator implements Validator<String, Map<String, String>> {

    private final Pattern pattern;
    private final String invalidValueMsg;
    private final String requestAttrName;

    public SingleValueValidator(String regex, String invalidValueMsg, String requestAttrName) {
        this.pattern = Pattern.compile(regex);
        this.invalidValueMsg = invalidValueMsg;
        this.requestAttrName = requestAttrName;
    }

    @Override
    public Map<String, String> validate(String value) {
        final Map<String, String> errorsByMessages = new HashMap<>();
        final Matcher matcher = pattern.matcher(value);

        if (!isValid(matcher, pattern, value)) {
            errorsByMessages.put(requestAttrName, invalidValueMsg);
        }

        return errorsByMessages;
    }

}
