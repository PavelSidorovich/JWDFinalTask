package com.sidorovich.pavel.buber.api.calculator;

public interface BiCalculator<T, F, R> {

    R calculate(T object1, F object2);

}
