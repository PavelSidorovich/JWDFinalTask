package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.validator.BiValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverRegisterValidator implements BiValidator<Driver, String, Map<String, String>> {

    private static final String INVALID_DRIVER_LICENCE =
            "Valid driver licence is required (should be like '1VE 255555')";
    private static final String DRIVER_LICENCE_PARAM_NAME = "drivingLicence";
    private static final String DRIVER_LICENCE_REGEX = "\\d[A-Z]{2} \\d{6}";

    private final UserRegisterValidator userRegisterValidator;
    private final TaxiValidator taxiValidator;

    private DriverRegisterValidator() {
        this.userRegisterValidator = UserRegisterValidator.getInstance();
        this.taxiValidator = TaxiValidator.getInstance();
    }

    private static class Holder {
        private static final DriverRegisterValidator INSTANCE = new DriverRegisterValidator();
    }

    public static DriverRegisterValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(Driver driver, String passwordRepeat) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(userRegisterValidator.validate(driver.getUser(), passwordRepeat));
        errorsByMessages.putAll(checkDriverLicence(driver));
        errorsByMessages.putAll(taxiValidator.validate(driver.getTaxi()));

        return errorsByMessages;
    }

    private Map<String, String> checkDriverLicence(Driver driver) {
        Map<String, String> errorsByMessages = new HashMap<>();
        Matcher matcher = Pattern.compile(DRIVER_LICENCE_REGEX).matcher(driver.getDrivingLicence());

        if (!matcher.matches()) {
            errorsByMessages.put(DRIVER_LICENCE_PARAM_NAME, INVALID_DRIVER_LICENCE);
        }

        return errorsByMessages;
    }


}
