package com.sidorovich.pavel.buber.core.calculator;

import com.sidorovich.pavel.buber.api.calculator.DistanceCalculator;
import com.sidorovich.pavel.buber.api.model.Coordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.*;

public class DistanceCalculatorImpl implements DistanceCalculator {

    private static final int DEGREES = 180;
    private static final int EARTH_RADIUS = 6371;
    private static final int SCALE = 2;

    private DistanceCalculatorImpl() {
    }

    @Override
    public BigDecimal calculate(Coordinates initial, Coordinates end) {
        BigDecimal rawDistance = calculateDistanceBetweenTwoPoints(initial, end);

        return rawDistance.multiply(new BigDecimal(EARTH_RADIUS)).setScale(SCALE, RoundingMode.CEILING);
    }

    private BigDecimal calculateDistanceBetweenTwoPoints(Coordinates initial, Coordinates end) {
        Double fromLatitude = initial.getLatitude().doubleValue();
        Double fromLongitude = initial.getLongitude().doubleValue();
        Double toLatitude = end.getLatitude().doubleValue();
        Double toLongitude = end.getLongitude().doubleValue();

        return BigDecimal.valueOf(acos(sin(toRad(toLatitude))
                                       * sin(toRad(fromLatitude))
                                       + cos(toRad(toLatitude))
                                         * cos(toRad(fromLatitude))
                                         * cos(toRad(toLongitude - fromLongitude))));
    }

    private Double toRad(Double degree) {
        return degree / DEGREES * PI;
    }

    public static DistanceCalculatorImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DistanceCalculatorImpl INSTANCE = new DistanceCalculatorImpl();
    }

}

