package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TaxiValidator implements Validator<Taxi, Map<String, String>> {

    private final Validator<Taxi, Map<String, String>> carNameValidator;
    private final Validator<String, Map<String, String>> carLicencePlateValidator;
    private final Validator<String, Map<String, String>> imageExtValidator;

    private TaxiValidator(
            Validator<Taxi, Map<String, String>> carNameValidator,
            Validator<String, Map<String, String>> carLicencePlateValidator,
            Validator<String, Map<String, String>> imageExtValidator) {
        this.carNameValidator = carNameValidator;
        this.carLicencePlateValidator = carLicencePlateValidator;
        this.imageExtValidator = imageExtValidator;
    }

    @Override
    public Map<String, String> validate(Taxi taxi, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>(carNameValidator.validate(taxi, resourceBundle));

        errorsByMessages.putAll(carLicencePlateValidator.validate(taxi.getLicencePlate(), resourceBundle));
        errorsByMessages.putAll(imageExtValidator.validate(taxi.getPhotoFilepath(), resourceBundle));

        return errorsByMessages;
    }

    public static TaxiValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TaxiValidator INSTANCE = new TaxiValidator(
                CarNameValidator.getInstance(),
                CarLicencePlateValidator.getInstance(),
                ImageExtValidator.getInstance()
        );
    }

}
