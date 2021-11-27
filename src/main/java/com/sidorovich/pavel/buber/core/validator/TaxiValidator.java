package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaxiValidator implements Validator<Taxi, Map<String, String>> {

    private static final String LICENCE_PLATE_PARAM_NAME = "licencePlate";
    private static final String CAR_PHOTO_PARAM_NAME = "carPhoto";
    private static final String INVALID_CAR_LICENCE_PLATE_MSG =
            "Valid car licence plate is required (should be like '4524 AX-7')";
    private static final String LICENCE_PLATE_REGEX = "\\d{4} [A-Z]{2}-[1-7]";
    private static final String INVALID_CAR_BRAND_MSG = "Valid car brand is required.";
    private static final String INVALID_CAR_MODEL_MSG = "Valid car model is required.";
    private static final String INVALID_CAR_PHOTO_MSG = "Valid car photo is required.";
    private static final String CAR_BRAND_PARAM_NAME = "carBrand";
    private static final String CAR_MODEL_PARAM_NAME = "carModel";

    private TaxiValidator() {
    }

    private static class Holder {
        private static final TaxiValidator INSTANCE = new TaxiValidator();
    }

    public static TaxiValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(Taxi taxi) {
        Map<String, String> errorsByMessages = new HashMap<>();
        Matcher matcher = Pattern.compile(LICENCE_PLATE_REGEX).matcher(taxi.getLicensePlate());

        if (taxi.getCarBrand().isEmpty()) {
            errorsByMessages.put(CAR_BRAND_PARAM_NAME, INVALID_CAR_BRAND_MSG);
        }
        if (taxi.getCarModel().isEmpty()) {
            errorsByMessages.put(CAR_MODEL_PARAM_NAME, INVALID_CAR_MODEL_MSG);
        }
        if (!matcher.matches()) {
            errorsByMessages.put(LICENCE_PLATE_PARAM_NAME, INVALID_CAR_LICENCE_PLATE_MSG);
        }
        if (taxi.getPhotoFilepath().isEmpty()) {
            errorsByMessages.put(CAR_PHOTO_PARAM_NAME, INVALID_CAR_PHOTO_MSG);
        }

        return errorsByMessages;
    }

}
