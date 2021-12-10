package com.sidorovich.pavel.buber.core.validator;

public class DrivingLicenceValidator extends SingleValueValidator {

    private static final String DRIVER_LICENCE_REGEX = "^\\d[A-Z]{2} \\d{6}$";
    private static final String INVALID_DRIVER_LICENCE_MSG =
            "Valid driver licence is required (should be like '1VE 255555')";
    private static final String DRIVER_LICENCE_ATTR_NAME = "drivingLicence";

    private DrivingLicenceValidator() {
        super(DRIVER_LICENCE_REGEX, INVALID_DRIVER_LICENCE_MSG, DRIVER_LICENCE_ATTR_NAME);
    }

    public static DrivingLicenceValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DrivingLicenceValidator INSTANCE = new DrivingLicenceValidator();
    }

}
