package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterValidator implements BiValidator<BuberUser, String, Map<String, String>> {

    private static final String EMPTY_STRING = "";
    private static final boolean EMAIL_IS_OPTIONAL = true;

    private final Validator<String, Map<String, String>> phoneValidator;
    private final Validator<BuberUser, Map<String, String>> personalInfoValidator;
    private final BiValidator<String, String, Map<String, String>> passwordValidator;
    private final BiValidator<String, Boolean, Map<String, String>> emailValidator;

    private UserRegisterValidator(
            Validator<String, Map<String, String>> phoneValidator,
            Validator<BuberUser, Map<String, String>> personalInfoValidator,
            BiValidator<String, String, Map<String, String>> passwordValidator,
            BiValidator<String, Boolean, Map<String, String>> emailValidator) {
        this.phoneValidator = phoneValidator;
        this.personalInfoValidator = personalInfoValidator;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
    }

    @Override
    public Map<String, String> validate(BuberUser user, String passwordRepeat) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(personalInfoValidator.validate(user));
        errorsByMessages.putAll(phoneValidator.validate(user.getPhone()));
        errorsByMessages.putAll(passwordValidator.validate(user.getPasswordHash(), passwordRepeat));
        errorsByMessages.putAll(emailValidator.validate(user.getEmail().orElse(EMPTY_STRING), EMAIL_IS_OPTIONAL));

        return errorsByMessages;
    }

    public static UserRegisterValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UserRegisterValidator INSTANCE =
                new UserRegisterValidator(
                        PhoneValidator.getInstance(),
                        PersonalInfoValidator.getInstance(),
                        PasswordValidator.getInstance(),
                        EmailValidator.getInstance()
                );
    }

}
