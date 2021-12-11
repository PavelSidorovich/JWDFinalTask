package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarNameValidator implements Validator<Taxi, Map<String, String>> {

    private static final String CAR_NAME_REGEX = "^[A-zА-Я0-9 ]{2,255}$";
    private static final String CAR_MODEL_ATTR_PARAM_NAME = "carModel";
    private static final String CAR_BRAND_ATTR_PARAM_NAME = "carBrand";
    private static final String INVALID_CAR_BRAND_KEY = "msg.invalid.carBrand";
    private static final String INVALID_CAR_MODEL_KEY = "msg.invalid.carModel";

    private CarNameValidator() {
    }

    @Override
    public Map<String, String> validate(Taxi taxi, ResourceBundle resourceBundle) {
        Pattern pattern = Pattern.compile(CAR_NAME_REGEX);
        Matcher matcher = pattern.matcher(taxi.getCarBrand());

        return new HashMap<>(validateCarInfo(taxi, pattern, matcher, resourceBundle));
    }

    private Map<String, String> validateCarInfo(Taxi taxi, Pattern pattern,
                                                Matcher matcher, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (!isValid(matcher, pattern, taxi.getCarBrand())) {
            errorsByMessages.put(CAR_BRAND_ATTR_PARAM_NAME, resourceBundle.getString(INVALID_CAR_BRAND_KEY));
        }
        if (!isValid(matcher, pattern, taxi.getCarModel())) {
            errorsByMessages.put(CAR_MODEL_ATTR_PARAM_NAME, resourceBundle.getString(INVALID_CAR_MODEL_KEY));
        }

        return errorsByMessages;
    }

    public static CarNameValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CarNameValidator INSTANCE = new CarNameValidator();
    }

}