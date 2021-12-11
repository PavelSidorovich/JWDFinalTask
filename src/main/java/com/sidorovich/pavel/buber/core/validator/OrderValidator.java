package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderValidator implements Validator<UserOrder, Map<String, String>> {

    private static final int COORDINATE_SCALE = 6;
    private static final String TAXI_PARAM_NAME = "taxi";
    private static final String LATITUDE_PARAM_NAME = "endLatitude";
    private static final String LONGITUDE_PARAM_NAME = "endLongitude";
    private static final String END_COORDINATES_PARAM_NAME = "endCoordinates";
    private static final String FUNDS_REQUEST_PARAM_NAME = "funds";
    private static final BigDecimal LEFT_LONGITUDE_BOUND_OF_MINSK = new BigDecimal("27.40676879882813");
    private static final BigDecimal RIGHT_LONGITUDE_BOUND_OF_MINSK = new BigDecimal("27.68692016601563");
    private static final BigDecimal BOTTOM_LATITUDE_BOUND_OF_MINSK = new BigDecimal("53.832675494023555");
    private static final BigDecimal TOP_LATITUDE_BOUND_OF_MINSK = new BigDecimal("53.96739671749269");
    private static final String INVALID_EMPTY_TAXI_KEY = "msg.invalid.emptyTaxi";
    private static final String DRIVER_BUSY_KEY = "msg.driverBusy";
    private static final String INVALID_LATITUDE_KEY = "msg.invalid.latitude";
    private static final String INVALID_LONGITUDE_KEY = "msg.invalid.longitude";
    private static final String INVALID_SAME_POINT_KEY = "msg.invalid.samePoint";
    private static final String NOT_ENOUGH_FUNDS_KEY = "msg.notEnoughFunds";

    private OrderValidator() {
    }

    @Override
    public Map<String, String> validate(UserOrder order, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(checkDriver(order.getDriver(), resourceBundle));
        errorsByMessages.putAll(checkCoordinates(order.getInitialCoordinates(),
                                                 order.getEndCoordinates(), resourceBundle));
        errorsByMessages.putAll(checkFunds(order, resourceBundle));

        return errorsByMessages;
    }

    private Map<String, String> checkDriver(Driver driver, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (driver == null) {
            errorsByMessages.put(TAXI_PARAM_NAME, resourceBundle.getString(INVALID_EMPTY_TAXI_KEY));
        } else if (driver.getDriverStatus() != DriverStatus.FREE) {
            errorsByMessages.put(TAXI_PARAM_NAME, resourceBundle.getString(DRIVER_BUSY_KEY));
        }

        return errorsByMessages;
    }

    private Map<String, String> checkCoordinates(Coordinates initialCoordinates,
                                                 Coordinates endCoordinates, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();
        BigDecimal latitude = endCoordinates.getLatitude();
        BigDecimal longitude = endCoordinates.getLongitude();

        if (latitude.compareTo(BOTTOM_LATITUDE_BOUND_OF_MINSK) < 0
            || latitude.compareTo(TOP_LATITUDE_BOUND_OF_MINSK) > 0) {
            errorsByMessages.put(LATITUDE_PARAM_NAME, resourceBundle.getString(INVALID_LATITUDE_KEY));
        }
        if (longitude.compareTo(LEFT_LONGITUDE_BOUND_OF_MINSK) < 0
            || longitude.compareTo(RIGHT_LONGITUDE_BOUND_OF_MINSK) > 0) {
            errorsByMessages.put(LONGITUDE_PARAM_NAME, resourceBundle.getString(INVALID_LONGITUDE_KEY));
        }
        if (coordinatesAreSame(initialCoordinates.getLatitude(), latitude)
            && coordinatesAreSame(initialCoordinates.getLongitude(), longitude)) {
            errorsByMessages.put(END_COORDINATES_PARAM_NAME, resourceBundle.getString(INVALID_SAME_POINT_KEY));
        }

        return errorsByMessages;
    }

    private boolean coordinatesAreSame(BigDecimal coordinate1, BigDecimal coordinate2) {
        return coordinate1
                       .setScale(COORDINATE_SCALE, RoundingMode.FLOOR)
                       .compareTo(coordinate2.setScale(COORDINATE_SCALE, RoundingMode.FLOOR)) == 0;
    }

    private Map<String, String> checkFunds(UserOrder order, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (order.getPrice().compareTo(order.getClient().getCash()) > 0) {
            errorsByMessages.put(FUNDS_REQUEST_PARAM_NAME, resourceBundle.getString(NOT_ENOUGH_FUNDS_KEY));
        }

        return errorsByMessages;
    }

    public static OrderValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OrderValidator INSTANCE = new OrderValidator();
    }

}
