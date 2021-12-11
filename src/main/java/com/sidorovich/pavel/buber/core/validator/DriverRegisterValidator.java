package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DriverRegisterValidator implements BiValidator<Driver, String, Map<String, String>> {

    private static final String EMPTY_STRING = "";
    private static final boolean EMAIL_IS_OPTIONAL = false;

    private final BiValidator<BuberUser, String, Map<String, String>> userRegisterValidator;
    private final BiValidator<String, Boolean, Map<String, String>> emailValidator;
    private final Validator<String, Map<String, String>> drivingLicenceValidator;
    private final Validator<Taxi, Map<String, String>> taxiValidator;

    private DriverRegisterValidator(
            BiValidator<BuberUser, String, Map<String, String>> userRegisterValidator,
            BiValidator<String, Boolean, Map<String, String>> emailValidator,
            Validator<String, Map<String, String>> drivingLicenceValidator,
            Validator<Taxi, Map<String, String>> taxiValidator) {
        this.userRegisterValidator = userRegisterValidator;
        this.emailValidator = emailValidator;
        this.drivingLicenceValidator = drivingLicenceValidator;
        this.taxiValidator = taxiValidator;
    }

    @Override
    public Map<String, String> validate(Driver driver, String passwordRepeat, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(userRegisterValidator.validate(driver.getUser(), passwordRepeat, resourceBundle));
        errorsByMessages.putAll(emailValidator.validate(driver.getUser().getEmail()
                                                              .orElse(EMPTY_STRING),
                                                        EMAIL_IS_OPTIONAL, resourceBundle));
        errorsByMessages.putAll(drivingLicenceValidator.validate(driver.getDrivingLicence(), resourceBundle));
        errorsByMessages.putAll(taxiValidator.validate(driver.getTaxi(), resourceBundle));

        return errorsByMessages;
    }

    public static DriverRegisterValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DriverRegisterValidator INSTANCE = new DriverRegisterValidator(
                UserRegisterValidator.getInstance(),
                EmailValidator.getInstance(),
                DrivingLicenceValidator.getInstance(),
                TaxiValidator.getInstance()
        );
    }

}
