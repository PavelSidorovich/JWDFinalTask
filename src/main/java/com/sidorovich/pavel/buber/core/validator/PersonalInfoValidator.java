package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalInfoValidator implements Validator<BuberUser, Map<String, String>> {

    private static final String NAME_REGEX = "[a-zA-Zа-яА-Я]{2,255}";
    private static final String F_NAME_ATTR_PARAM_NAME = "fName";
    private static final String L_NAME_ATTR_PARAM_NAME = "lName";
    private static final String INVALID_F_NAME_KEY = "msg.invalid.fName";
    private static final String INVALID_L_NAME_KEY = "msg.invalid.lName";

    private PersonalInfoValidator() {
    }

    @Override
    public Map<String, String> validate(BuberUser user, ResourceBundle resourceBundle) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(user.getFirstName());

        return new HashMap<>(validatePersonalInfo(user, pattern, matcher, resourceBundle));
    }

    private Map<String, String> validatePersonalInfo(BuberUser user, Pattern pattern,
                                                     Matcher matcher, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (!isValid(matcher, pattern, user.getFirstName())) {
            errorsByMessages.put(F_NAME_ATTR_PARAM_NAME, resourceBundle.getString(INVALID_F_NAME_KEY));
        }
        if (!isValid(matcher, pattern, user.getLastName())) {
            errorsByMessages.put(L_NAME_ATTR_PARAM_NAME, resourceBundle.getString(INVALID_L_NAME_KEY));
        }

        return errorsByMessages;
    }

    public static PersonalInfoValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PersonalInfoValidator INSTANCE = new PersonalInfoValidator();
    }

}