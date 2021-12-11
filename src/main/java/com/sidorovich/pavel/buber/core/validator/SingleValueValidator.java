package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleValueValidator implements Validator<String, Map<String, String>> {

    private final Pattern pattern;
    private final String requestAttrName;
    private final String resourceBundleKey;

    public SingleValueValidator(String regex, String resourceBundleKey, String requestAttrName) {
        this.pattern = Pattern.compile(regex);
        this.requestAttrName = requestAttrName;
        this.resourceBundleKey = resourceBundleKey;
    }

    @Override
    public Map<String, String> validate(String value, ResourceBundle resourceBundle) {
        final Map<String, String> errorsByMessages = new HashMap<>();
        final Matcher matcher = pattern.matcher(value);

        if (!isValid(matcher, pattern, value)) {
            errorsByMessages.put(requestAttrName, resourceBundle.getString(resourceBundleKey));
        }

        return errorsByMessages;
    }

}
