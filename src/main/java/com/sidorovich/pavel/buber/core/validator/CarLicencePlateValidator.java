package com.sidorovich.pavel.buber.core.validator;

public class CarLicencePlateValidator extends SingleValueValidator {

    private static final String CAR_LICENCE_PLATE_REGEX = "^\\d{4} [A-Z]{2}-[1-7]$";
    private static final String LICENCE_PLATE_ATTR_PARAM_NAME = "licencePlate";
    private static final String INVALID_LICENCE_PLATE_KEY = "msg.invalid.carLicencePlate";

    private CarLicencePlateValidator() {
        super(CAR_LICENCE_PLATE_REGEX,
              INVALID_LICENCE_PLATE_KEY,
              LICENCE_PLATE_ATTR_PARAM_NAME);
    }

    public static CarLicencePlateValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CarLicencePlateValidator INSTANCE = new CarLicencePlateValidator();
    }

}
