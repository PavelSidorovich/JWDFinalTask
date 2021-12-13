package com.sidorovich.pavel.buber.core.calculator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class PriceCalculatorImplTest {

    private static final int DELTA = 2;

    private final PriceCalculatorImpl priceCalculator = PriceCalculatorImpl.getInstance();

    @DataProvider(name = "DataProvider")
    public Object[][] getInputValuesAndPrice() {
        return new Object[][] {
                {
                        new BigDecimal(10),
                        10d,
                        new BigDecimal("20.69")
                },
                {
                        new BigDecimal(3),
                        34d,
                        new BigDecimal("4.57")
                }
        };
    }

    @Test(dataProvider = "DataProvider")
    public void calculate_shouldReturnPriceOfOrder_always(BigDecimal distance,
                                                          Double discount,
                                                          BigDecimal price) {
        BigDecimal actualPrice = priceCalculator.calculate(distance, discount);

        assertEquals(actualPrice.doubleValue(), price.doubleValue(), DELTA);
    }

}