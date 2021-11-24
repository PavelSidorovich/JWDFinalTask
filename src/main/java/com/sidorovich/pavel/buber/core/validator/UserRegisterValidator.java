package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegisterValidator implements Validator<BuberUser, String> {

    private static final String PHONE_REGEX = "[+]375[ ]\\d{2}[ ]\\d{3}[-]\\d{2}[-]\\d{2}";
    private static final String NAME_REGEX = "[a-zA-Zа-яА-Я]{2,}";
    private static final String INCORRECT_FIRST_NAME_MSG = "Incorrect first name";
    private static final String INCORRECT_LAST_NAME_MSG = "Incorrect last name";
    private static final String INCORRECT_PHONE_MSG = "Incorrect phone";
    private static final String USER_ALREADY_EXISTS_MSG = "User with this phone already exists";

    private final UserService userService;

    private UserRegisterValidator(UserService userService) {
        this.userService = userService;
    }

    private static class Holder {
        private static final UserRegisterValidator INSTANCE =
                new UserRegisterValidator(
                        EntityServiceFactory.getInstance().serviceFor(UserService.class)
                );
    }

    public static UserRegisterValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String validate(BuberUser user) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(user.getFirstName());
        String errorMsg = getErrorMsg(user, pattern, matcher);

        if (errorMsg != null) {
            return errorMsg;
        }
        try {
            userService.save(user);
            return null;
        } catch (SQLException e) {
            return USER_ALREADY_EXISTS_MSG;
        }
    }

    private String getErrorMsg(BuberUser user, Pattern pattern, Matcher matcher) {
        if (!isValid(matcher, pattern, user.getFirstName())) {
            return INCORRECT_FIRST_NAME_MSG;
        }
        if (!isValid(matcher, pattern, user.getLastName())) {
            return INCORRECT_LAST_NAME_MSG;
        }
        if (!isValid(matcher, Pattern.compile(PHONE_REGEX), user.getPhone())) {
            return INCORRECT_PHONE_MSG;
        }
        return null;
    }

}
