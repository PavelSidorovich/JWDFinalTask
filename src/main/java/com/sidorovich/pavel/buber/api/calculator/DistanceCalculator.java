package com.sidorovich.pavel.buber.api.calculator;

import com.sidorovich.pavel.buber.api.model.Coordinates;

import java.math.BigDecimal;

public interface DistanceCalculator {

    BigDecimal calculate(Coordinates initial, Coordinates end);

}
