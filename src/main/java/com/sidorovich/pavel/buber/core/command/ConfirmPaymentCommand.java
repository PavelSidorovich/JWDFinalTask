package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.OrderService;
import com.sidorovich.pavel.buber.core.service.TaxiService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class ConfirmPaymentCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";

    private final OrderService orderService;
    private final UserService userService;
    private final DriverService driverService;
    private final TaxiService taxiService;

    private ConfirmPaymentCommand(RequestFactory requestFactory,
                                  OrderService orderService,
                                  UserService userService,
                                  DriverService driverService,
                                  TaxiService taxiService) {
        super(requestFactory);
        this.orderService = orderService;
        this.userService = userService;
        this.driverService = driverService;
        this.taxiService = taxiService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElse(null);

        if (account != null) {
            Optional<Driver> driver = driverService.findById(account.getId().orElse(-1L));

            if (driver.isPresent()) {
                Optional<UserOrder> driverOrder = findCurrentDriverOrder(driver.get());

                driverOrder.ifPresent(order -> confirmPayment(driverOrder.get()));
            }
        }

        return requestFactory.createRedirectResponse(PagePaths.INCOMING_ORDER.getCommand());
    }

    private void confirmPayment(UserOrder order) {
        debitClientCash(order);
        updateDriverInfo(order);
        orderService.update(
                order.withStatus(OrderStatus.COMPLETED)
                     .withDateOfTrip(Date.valueOf(LocalDate.now()))
        );
    }

    private void updateDriverInfo(UserOrder order) {
        Driver driver = order.getDriver();
        Taxi taxi = driver.getTaxi().withLastCoordinates(order.getEndCoordinates());
        BuberUser driverUser = driver.getUser();

        BuberUser withCash = driverUser.withCash(driverUser.getCash().add(order.getPrice()));
        taxiService.update(taxi);
        driverService.update(driver.withBuberUser(withCash)
                                   .withDriverStatus(DriverStatus.FREE));
    }

    private void debitClientCash(UserOrder order) {
        BuberUser client = order.getClient();

        userService.update(client.withCash(client.getCash()
                                                 .subtract(order.getPrice())));
    }

    private Optional<UserOrder> findCurrentDriverOrder(Driver driver) {
        return orderService
                .findByDriver(driver)
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.IN_PROCESS)
                .findFirst();
    }

    public static ConfirmPaymentCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {

        private static final EntityServiceFactory ENTITY_SERVICE_FACTORY = EntityServiceFactory.getInstance();

        private static final ConfirmPaymentCommand INSTANCE = new ConfirmPaymentCommand(
                RequestFactoryImpl.getInstance(),
                ENTITY_SERVICE_FACTORY.serviceFor(OrderService.class),
                ENTITY_SERVICE_FACTORY.serviceFor(UserService.class),
                ENTITY_SERVICE_FACTORY.serviceFor(DriverService.class),
                ENTITY_SERVICE_FACTORY.serviceFor(TaxiService.class)
        );
    }

}
