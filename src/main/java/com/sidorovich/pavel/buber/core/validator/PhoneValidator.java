package com.sidorovich.pavel.buber.core.validator;

public class PhoneValidator extends SingleValueValidator {

    private static final String PHONE_REGEX = "^[+]375[ ]\\d{2}[ ]\\d{3}[-]\\d{2}[-]\\d{2}$";
    private static final String INVALID_PHONE_MSG = "Valid phone is required";
    private static final String PHONE_ATTR_PARAM_NAME = "phone";

    private PhoneValidator() {
        super(PHONE_REGEX, INVALID_PHONE_MSG, PHONE_ATTR_PARAM_NAME);
    }

    public static PhoneValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PhoneValidator INSTANCE = new PhoneValidator();
    }

}
