package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.Map;

public class CashValidator extends SingleValueValidator implements Validator<String, Map<String, String>> {

    private static final String CASH_REGEX = "^\\d{1,6}[.]?\\d{0,2}$";
    private static final String CASH_ATTR_PARAM_NAME = "cash";
    private static final String INVALID_CASH_MSG = "Valid cash value is required (for example, 500.00)";

    private CashValidator() {
        super(CASH_REGEX, INVALID_CASH_MSG, CASH_ATTR_PARAM_NAME);
    }

    public static CashValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CashValidator INSTANCE = new CashValidator();
    }

}
