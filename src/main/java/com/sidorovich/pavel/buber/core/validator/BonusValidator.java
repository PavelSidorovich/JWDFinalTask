package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BonusValidator implements Validator<Bonus, Map<String, String>> {

    private static final String EXPIRE_DATE_PARAM_NAME = "expireDate";
    private static final String VALID_DATA_IS_REQUIRED = "Valid data is required";
    private static final String DISCOUNT_PARAM_NAME = "discount";
    private static final String DISCOUNT_RANGE_ERROR = "Discount value should be in range (0..100%)";

    private BonusValidator() {
    }

    private static class Holder {
        private static final BonusValidator INSTANCE = new BonusValidator();
    }

    public static BonusValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(Bonus bonus) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(checkExpirationDate(bonus.getExpireDate()));
        errorsByMessages.putAll(checkDiscount(bonus.getDiscount()));

        return errorsByMessages;
    }

    private Map<String, String> checkDiscount(Double discount) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (discount.compareTo(0d) < 0 || discount.compareTo(100d) > 0) {
            errorsByMessages.put(DISCOUNT_PARAM_NAME, DISCOUNT_RANGE_ERROR);
        }

        return errorsByMessages;
    }

    private Map<String, String> checkExpirationDate(Date expirationDate) {
        Map<String, String> errorsByMessages = new HashMap<>();
        Date dateNow = Date.valueOf(LocalDate.now());

        if (dateNow.after(expirationDate)) {
            errorsByMessages.put(EXPIRE_DATE_PARAM_NAME, VALID_DATA_IS_REQUIRED);
        }

        return errorsByMessages;
    }

}
