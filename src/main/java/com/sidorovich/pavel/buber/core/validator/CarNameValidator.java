package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarNameValidator implements Validator<Taxi, Map<String, String>> {

    private static final String CAR_NAME_REGEX = "^[A-zА-Я0-9 ]{2,255}$";
    private static final String CAR_MODEL_ATTR_PARAM_NAME = "carModel";
    private static final String CAR_BRAND_ATTR_PARAM_NAME = "carBrand";
    private static final String INVALID_CAR_MODEL_MSG = "Valid car model is required";
    private static final String INVALID_CAR_BRAND_MSG = "Valid car brand is required";

    private CarNameValidator() {
    }

    @Override
    public Map<String, String> validate(Taxi taxi) {
        Pattern pattern = Pattern.compile(CAR_NAME_REGEX);
        Matcher matcher = pattern.matcher(taxi.getCarBrand());

        return new HashMap<>(validateCarInfo(taxi, pattern, matcher));
    }

    private Map<String, String> validateCarInfo(Taxi taxi, Pattern pattern, Matcher matcher) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (!isValid(matcher, pattern, taxi.getCarBrand())) {
            errorsByMessages.put(CAR_BRAND_ATTR_PARAM_NAME, INVALID_CAR_BRAND_MSG);
        }
        if (!isValid(matcher, pattern, taxi.getCarModel())) {
            errorsByMessages.put(CAR_MODEL_ATTR_PARAM_NAME, INVALID_CAR_MODEL_MSG);
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