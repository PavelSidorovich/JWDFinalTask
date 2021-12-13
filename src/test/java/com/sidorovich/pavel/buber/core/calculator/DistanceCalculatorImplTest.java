package com.sidorovich.pavel.buber.core.calculator;

import com.sidorovich.pavel.buber.api.model.Coordinates;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class DistanceCalculatorImplTest {

    private static final int DELTA = 6;

    private final DistanceCalculatorImpl distanceCalculator = DistanceCalculatorImpl.getInstance();

    @DataProvider(name = "DataProvider")
    public Object[][] getCoordinatesAndDistance() {
        return new Object[][] {
                {
                        new Coordinates(new BigDecimal("53.883174322461750"),
                                        new BigDecimal("27.673199232331480")),
                        new Coordinates(new BigDecimal("53.883174322461750"),
                                        new BigDecimal("27.673199232331480")),
                        BigDecimal.ZERO
                },
                {
                        new Coordinates(new BigDecimal("53.901544298094490"),
                                        new BigDecimal("27.561006546020510")),
                        new Coordinates(new BigDecimal("53.883174322461750"),
                                        new BigDecimal("27.673199232331480")),
                        new BigDecimal("7.64")
                }
        };
    }

    @Test(dataProvider = "DataProvider")
    public void calculate_shouldReturnDistanceOfRoute_always(Coordinates initial,
                                                             Coordinates end,
                                                             BigDecimal distance) {
        BigDecimal actualDistance = distanceCalculator.calculate(initial, end);

        assertEquals(actualDistance.doubleValue(), distance.doubleValue(), DELTA);
    }

}