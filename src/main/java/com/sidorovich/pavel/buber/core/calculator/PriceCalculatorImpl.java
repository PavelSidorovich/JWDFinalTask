package com.sidorovich.pavel.buber.core.calculator;

import com.sidorovich.pavel.buber.api.calculator.BiCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculatorImpl implements BiCalculator<BigDecimal, Double, BigDecimal> {

    private static final BigDecimal CAR_FUEL_CONSUMPTION = BigDecimal.valueOf(10);
    private static final BigDecimal PERCENTS = new BigDecimal(100);
    private static final BigDecimal FUEL_PRICE = new BigDecimal("2.09");
    private static final BigDecimal SHARE_OF_FUEL_IN_THE_PRICE = BigDecimal.TEN;
    private static final int ROUNDING_SCALE = 2;

    private PriceCalculatorImpl() {
    }

    @Override
    public BigDecimal calculate(BigDecimal distance, Double bonus) {
        BigDecimal moneyForFuel = calculateMoneyForFuel(distance);
        BigDecimal moneyForDriver = calculateMoneyForDriver(moneyForFuel);
        BigDecimal totalPriceWithoutDiscount = calculateTotalPriceWithoutBonus(moneyForFuel, moneyForDriver);

        return calculateTotalPrice(bonus, totalPriceWithoutDiscount);
    }

    private BigDecimal calculateTotalPrice(Double bonus, BigDecimal totalPriceWithoutDiscount) {
        return totalPriceWithoutDiscount
                .multiply(PERCENTS.subtract(BigDecimal.valueOf(bonus)))
                .divide(PERCENTS, ROUNDING_SCALE, RoundingMode.FLOOR);
    }

    private BigDecimal calculateTotalPriceWithoutBonus(BigDecimal moneyForFuel, BigDecimal moneyForDriver) {
        return moneyForFuel
                .add(moneyForDriver);
    }

    private BigDecimal calculateMoneyForDriver(BigDecimal moneyForFuel) {
        return moneyForFuel
                .multiply(SHARE_OF_FUEL_IN_THE_PRICE);
    }

    private BigDecimal calculateMoneyForFuel(BigDecimal distance) {
        return FUEL_PRICE
                .multiply(CAR_FUEL_CONSUMPTION)
                .multiply(distance)
                .divide(PERCENTS, RoundingMode.CEILING);
    }

    public static PriceCalculatorImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PriceCalculatorImpl INSTANCE = new PriceCalculatorImpl();
    }

}
