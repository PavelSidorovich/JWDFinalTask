package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BonusValidator implements Validator<Bonus, Map<String, String>> {

    private static final String EXPIRE_DATE_PARAM_NAME = "expireDate";
    private static final String DISCOUNT_PARAM_NAME = "discount";
    private static final String INVALID_DATA_KEY = "msg.invalid.data";
    private static final String INVALID_DISCOUNT_KEY = "msg.invalid.discount";

    private BonusValidator() {
    }

    private static class Holder {
        private static final BonusValidator INSTANCE = new BonusValidator();
    }

    public static BonusValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(Bonus bonus, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(checkExpirationDate(bonus.getExpireDate(), resourceBundle));
        errorsByMessages.putAll(checkDiscount(bonus.getDiscount(), resourceBundle));

        return errorsByMessages;
    }

    private Map<String, String> checkDiscount(Double discount, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (discount.compareTo(0d) < 0 || discount.compareTo(100d) > 0) {
            errorsByMessages.put(DISCOUNT_PARAM_NAME, resourceBundle.getString(INVALID_DISCOUNT_KEY));
        }

        return errorsByMessages;
    }

    private Map<String, String> checkExpirationDate(Date expirationDate, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();
        Date dateNow = Date.valueOf(LocalDate.now());

        if (dateNow.after(expirationDate)) {
            errorsByMessages.put(EXPIRE_DATE_PARAM_NAME, resourceBundle.getString(INVALID_DATA_KEY));
        }

        return errorsByMessages;
    }

}
