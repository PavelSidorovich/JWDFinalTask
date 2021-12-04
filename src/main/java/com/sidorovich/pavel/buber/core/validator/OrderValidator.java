package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.validator.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class OrderValidator implements Validator<UserOrder, Map<String, String>> {

    private static final int COORDINATE_SCALE = 6;
    private static final String TAXI_PARAM_NAME = "taxi";
    private static final String LATITUDE_PARAM_NAME = "endLatitude";
    private static final String LONGITUDE_PARAM_NAME = "endLongitude";
    private static final String END_COORDINATES_PARAM_NAME = "endCoordinates";
    private static final String FUNDS_REQUEST_PARAM_NAME = "funds";
    private static final String INVALID_END_COORDINATES_MSG = "Your location is the same as the end point!";
    private static final String TAXI_IS_NOT_SPECIFIED_MSG = "Please, choose taxi from list";
    private static final String LATITUDE_VALUE_IS_OUT_OF_BOUNDS_MSG = "Latitude value is out of Minsk bounds";
    private static final String LONGITUDE_VALUE_IS_OUT_OF_BOUNDS_MSG = "Longitude value is out of Minsk bounds";
    private static final String NOT_ENOUGH_FUNDS_MSG = "Not enough funds to make an order";
    private static final BigDecimal LEFT_LONGITUDE_BOUND_OF_MINSK = new BigDecimal("27.40676879882813");
    private static final BigDecimal RIGHT_LONGITUDE_BOUND_OF_MINSK = new BigDecimal("27.68692016601563");
    private static final BigDecimal BOTTOM_LATITUDE_BOUND_OF_MINSK = new BigDecimal("53.832675494023555");
    private static final BigDecimal TOP_LATITUDE_BOUND_OF_MINSK = new BigDecimal("53.96739671749269");

    private OrderValidator() {
    }

    @Override
    public Map<String, String> validate(UserOrder order) {
        Map<String, String> errorsByMessages = new HashMap<>();

        errorsByMessages.putAll(checkDriver(order.getDriver()));
        errorsByMessages.putAll(checkCoordinates(order.getInitialCoordinates(), order.getEndCoordinates()));
        errorsByMessages.putAll(checkFunds(order));

        return errorsByMessages;
    }

    private Map<String, String> checkDriver(Driver driver) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (driver == null) {
            errorsByMessages.put(TAXI_PARAM_NAME, TAXI_IS_NOT_SPECIFIED_MSG);
        }

        return errorsByMessages;
    }

    private Map<String, String> checkCoordinates(Coordinates initialCoordinates, Coordinates endCoordinates) {
        Map<String, String> errorsByMessages = new HashMap<>();
        BigDecimal latitude = endCoordinates.getLatitude();
        BigDecimal longitude = endCoordinates.getLongitude();

        if (latitude.compareTo(BOTTOM_LATITUDE_BOUND_OF_MINSK) < 0
            || latitude.compareTo(TOP_LATITUDE_BOUND_OF_MINSK) > 0) {
            errorsByMessages.put(LATITUDE_PARAM_NAME, LATITUDE_VALUE_IS_OUT_OF_BOUNDS_MSG);
        }
        if (longitude.compareTo(LEFT_LONGITUDE_BOUND_OF_MINSK) < 0
            || longitude.compareTo(RIGHT_LONGITUDE_BOUND_OF_MINSK) > 0) {
            errorsByMessages.put(LONGITUDE_PARAM_NAME, LONGITUDE_VALUE_IS_OUT_OF_BOUNDS_MSG);
        }
        if (coordinatesAreSame(initialCoordinates.getLatitude(), latitude)
            && coordinatesAreSame(initialCoordinates.getLongitude(), longitude)) {
            errorsByMessages.put(END_COORDINATES_PARAM_NAME, INVALID_END_COORDINATES_MSG);
        }

        return errorsByMessages;
    }

    private boolean coordinatesAreSame(BigDecimal coordinate1, BigDecimal coordinate2) {
        return coordinate1
                       .setScale(COORDINATE_SCALE, RoundingMode.FLOOR)
                       .compareTo(coordinate2.setScale(COORDINATE_SCALE, RoundingMode.FLOOR)) == 0;
    }

    private Map<String, String> checkFunds(UserOrder order) {
        Map<String, String> errorsByMessages = new HashMap<>();

        if (order.getPrice().compareTo(order.getClient().getCash()) > 0) {
            errorsByMessages.put(FUNDS_REQUEST_PARAM_NAME, NOT_ENOUGH_FUNDS_MSG);
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
