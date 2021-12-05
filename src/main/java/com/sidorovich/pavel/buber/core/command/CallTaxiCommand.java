package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.calculator.BiCalculator;
import com.sidorovich.pavel.buber.api.calculator.DistanceCalculator;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.calculator.DistanceCalculatorImpl;
import com.sidorovich.pavel.buber.core.calculator.PriceCalculatorImpl;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.dao.CoordinatesDao;
import com.sidorovich.pavel.buber.core.dao.DaoFactory;
import com.sidorovich.pavel.buber.core.dto.OrderDto;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.validator.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CallTaxiCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(CallTaxiCommand.class);

    private static final String PHONE_REQUEST_PARAM_NAME = "phone";
    private static final String INITIAL_LONGITUDE_REQUEST_PARAM_NAME = "initialLongitude";
    private static final String INITIAL_LATITUDE_REQUEST_PARAM_NAME = "initialLatitude";
    private static final String END_LONGITUDE_REQUEST_PARAM_NAME = "endLongitude";
    private static final String END_LATITUDE_REQUEST_PARAM_NAME = "endLatitude";
    private static final String TAXI_REQUEST_PARAM_NAME = "taxi";
    private static final String BONUS_REQUEST_PARAM_NAME = "bonus";
    private static final String INVALID_END_LONGITUDE_MSG = "Valid longitude is required";
    private static final String INVALID_END_LATITUDE_MSG = "Valid latitude is required";
    private static final String INVALID_ATTR_ENDING = "Invalid";

    //todo rename service
    private final UserOrderService orderService;
    private final DriverService driverService;
    private final UserService userService;
    private final CoordinatesDao coordinatesDao;
    private final Validator<UserOrder, Map<String, String>> orderValidator;
    private final BiCalculator<BigDecimal, Double, BigDecimal> priceCalculator;
    private final DistanceCalculator distanceCalculator;

    private CallTaxiCommand(RequestFactory requestFactory, UserOrderService orderService,
                            DriverService driverService, UserService userService,
                            CoordinatesDao coordinatesDao,
                            Validator<UserOrder, Map<String, String>> orderValidator,
                            BiCalculator<BigDecimal, Double, BigDecimal> priceCalculator,
                            DistanceCalculator distanceCalculator) {
        super(requestFactory);
        this.orderService = orderService;
        this.driverService = driverService;
        this.userService = userService;
        this.coordinatesDao = coordinatesDao;
        this.orderValidator = orderValidator;
        this.priceCalculator = priceCalculator;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (invalidateCoordinate(request, END_LONGITUDE_REQUEST_PARAM_NAME, INVALID_END_LONGITUDE_MSG)
            & invalidateCoordinate(request, END_LATITUDE_REQUEST_PARAM_NAME, INVALID_END_LATITUDE_MSG)) {
            Map<String, String> errorsByMessages = new HashMap<>(saveOrder(getOrderDto(request)));

            errorsByMessages.forEach(request::addAttributeToJsp);
            if (errorsByMessages.isEmpty()) {
                return requestFactory.createRedirectResponse(PagePaths.CLIENT_ORDER.getCommand());
            }
        }

        return requestFactory.createForwardResponse(PagePaths.CLIENT_ORDER.getCommand());
    }

    private Map<String, String> saveOrder(OrderDto orderDto) {
        Coordinates initialCoordinates = orderDto.getInitialCoordinates();
        Coordinates endCoordinates = orderDto.getEndCoordinates();
        BigDecimal distance = distanceCalculator.calculate(initialCoordinates, endCoordinates);
        BigDecimal price = priceCalculator.calculate(distance, orderDto.getBonus());

        try {
            UserOrder order = getUserOrder(orderDto, initialCoordinates, endCoordinates, price);
            Map<String, String> validate = orderValidator.validate(order);

            if (!validate.isEmpty()) {
                return validate;
            }
            driverService.update(order.getDriver().withDriverStatus(DriverStatus.BUSY));
            orderService.save(order);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return Collections.emptyMap();
    }

    private UserOrder getUserOrder(OrderDto orderDto, Coordinates initialCoordinates, Coordinates endCoordinates,
                                   BigDecimal price) throws SQLException {
        Coordinates savedInitialCoordinates = coordinatesDao.save(initialCoordinates);
        Coordinates savedEndCoordinates = coordinatesDao.save(endCoordinates);
        Driver driver = driverService.findByTaxiLicencePlate(orderDto.getLicencePlate()).orElse(null);

        if (driver == null) {
            coordinatesDao.delete(savedInitialCoordinates.getId().orElse(-1L));
            coordinatesDao.delete(savedEndCoordinates.getId().orElse(-1L));
        }

        return UserOrder.with()
                        .client(userService.findByPhone(orderDto.getPhone()).orElse(null))
                        .driver(driver)
                        .initialCoordinates(savedInitialCoordinates)
                        .endCoordinates(savedEndCoordinates)
                        .status(OrderStatus.NEW)
                        .price(price)
                        .build();
    }

    private OrderDto getOrderDto(CommandRequest request) {
        return new OrderDto(
                request.getParameter(PHONE_REQUEST_PARAM_NAME),
                request.getParameter(INITIAL_LONGITUDE_REQUEST_PARAM_NAME),
                request.getParameter(INITIAL_LATITUDE_REQUEST_PARAM_NAME),
                request.getParameter(END_LONGITUDE_REQUEST_PARAM_NAME),
                request.getParameter(END_LATITUDE_REQUEST_PARAM_NAME),
                request.getParameter(TAXI_REQUEST_PARAM_NAME),
                request.getParameter(BONUS_REQUEST_PARAM_NAME)
        );
    }

    private boolean invalidateCoordinate(CommandRequest request, String paramName, String errorMsg) {
        try {
            request.addAttributeToJsp(paramName, new BigDecimal(request.getParameter(paramName)));

            return true;
        } catch (NumberFormatException e) {
            request.addAttributeToJsp(paramName + INVALID_ATTR_ENDING, errorMsg);
        }

        return false;
    }

    private static class Holder {
        private static final EntityServiceFactory SERVICE_FACTORY = EntityServiceFactory.getInstance();
        private static final CallTaxiCommand INSTANCE = new CallTaxiCommand(
                RequestFactoryImpl.getInstance(),
                SERVICE_FACTORY.serviceFor(UserOrderService.class),
                SERVICE_FACTORY.serviceFor(DriverService.class),
                SERVICE_FACTORY.serviceFor(UserService.class),
                DaoFactory.getInstance().serviceFor(CoordinatesDao.class),
                OrderValidator.getInstance(), PriceCalculatorImpl.getInstance(),
                DistanceCalculatorImpl.getInstance());
    }

    public static CallTaxiCommand getInstance() {
        return Holder.INSTANCE;
    }

}
