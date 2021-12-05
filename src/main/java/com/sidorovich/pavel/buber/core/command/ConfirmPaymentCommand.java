package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.Optional;

public class ConfirmPaymentCommand extends CommonCommand {

    public static final String USER_SESSION_PARAM_NAME = "user";

    private final UserOrderService orderService;
    private final UserService userService;
    private final DriverService driverService;

    private ConfirmPaymentCommand(RequestFactory requestFactory,
                                  UserOrderService orderService,
                                  UserService userService,
                                  DriverService driverService) {
        super(requestFactory);
        this.orderService = orderService;
        this.userService = userService;
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElse(null);

            if (account != null) {
                Optional<Driver> driver = driverService.findById(account.getId().orElse(-1L));

                if (driver.isPresent()) {
                    Optional<UserOrder> driverOrder = findCurrentDriverOrder(driver.get());

                    driverOrder.ifPresent(order -> confirmPayment(driverOrder.get()));
                }
            }
        }

        return requestFactory.createRedirectResponse(PagePaths.INCOMING_ORDER.getCommand());
    }

    private void confirmPayment(UserOrder order) {
        debitClientCash(order);
        driverService.update(order.getDriver().withDriverStatus(DriverStatus.FREE));
        orderService.update(order.withStatus(OrderStatus.COMPLETED));
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
        private static final ConfirmPaymentCommand INSTANCE = new ConfirmPaymentCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class));
    }

}
