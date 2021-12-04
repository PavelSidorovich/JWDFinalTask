package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.calculator.BiCalculator;
import com.sidorovich.pavel.buber.api.calculator.DistanceCalculator;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.core.calculator.DistanceCalculatorImpl;
import com.sidorovich.pavel.buber.core.calculator.PriceCalculatorImpl;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

import java.math.BigDecimal;

public class GetOrderPriceCommand extends CommonCommand {

    private static final String INITIAL_LONGITUDE_REQUEST_PARAM_NAME = "initialLongitude";
    private static final String INITIAL_LATITUDE_REQUEST_PARAM_NAME = "initialLatitude";
    private static final String END_LONGITUDE_REQUEST_PARAM_NAME = "endLongitude";
    private static final String END_LATITUDE_REQUEST_PARAM_NAME = "endLatitude";
    private static final String BONUS_REQUEST_PARAM_NAME = "bonus";

    private final BiCalculator<BigDecimal, Double, BigDecimal> priceCalculator;
    private final DistanceCalculator distanceCalculator;

    private GetOrderPriceCommand(RequestFactory requestFactory,
                                 BiCalculator<BigDecimal, Double, BigDecimal> priceCalculator,
                                 DistanceCalculator distanceCalculator) {
        super(requestFactory);
        this.priceCalculator = priceCalculator;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            BigDecimal totalPrice = calculateTotalPrice(request);

            return requestFactory.createJsonResponse(totalPrice, JsonResponseStatus.SUCCESS);
        } catch (Exception e) {
            return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR);
        }
    }

    private BigDecimal calculateTotalPrice(CommandRequest request) {
        BigDecimal initialLongitude = getCoordinate(request, INITIAL_LONGITUDE_REQUEST_PARAM_NAME);
        BigDecimal initialLatitude = getCoordinate(request, INITIAL_LATITUDE_REQUEST_PARAM_NAME);
        BigDecimal endLongitude = getCoordinate(request, END_LONGITUDE_REQUEST_PARAM_NAME);
        BigDecimal endLatitude = getCoordinate(request, END_LATITUDE_REQUEST_PARAM_NAME);
        Coordinates initialPoint = new Coordinates(initialLatitude, initialLongitude);
        Coordinates endPoint = new Coordinates(endLatitude, endLongitude);
        BigDecimal distance = distanceCalculator.calculate(initialPoint, endPoint);
        Double bonus = getBonus(request);

        return priceCalculator.calculate(distance, bonus);
    }

    private Double getBonus(CommandRequest request) {
        try {
            return Double.parseDouble(request.getParameter(BONUS_REQUEST_PARAM_NAME));
        } catch (Exception e) {
            return 0d;
        }
    }

    private BigDecimal getCoordinate(CommandRequest request, String paramName) {
        return new BigDecimal(request.getParameter(paramName));
    }

    public static GetOrderPriceCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final GetOrderPriceCommand INSTANCE = new GetOrderPriceCommand(
                RequestFactoryImpl.getInstance(),
                PriceCalculatorImpl.getInstance(),
                DistanceCalculatorImpl.getInstance());
    }

}
