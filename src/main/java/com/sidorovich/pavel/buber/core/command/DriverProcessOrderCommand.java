package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.OrderService;

import java.util.Optional;

public class DriverProcessOrderCommand extends CommonCommand {

    public static final String USER_SESSION_PARAM_NAME = "user";
    public static final String ORDER_STATUS_REQUEST_PARAM_NAME = "orderStatus";

    private final OrderService orderService;
    private final DriverService driverService;

    private DriverProcessOrderCommand(RequestFactory requestFactory,
                                      OrderService orderService,
                                      DriverService driverService) {
        super(requestFactory);
        this.orderService = orderService;
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElse(null);
            String status = request.getParameter(ORDER_STATUS_REQUEST_PARAM_NAME);

            if (account != null) {
                Optional<Driver> driver = driverService.findById(account.getId().orElse(-1L));

                if (driver.isPresent()) {
                    Optional<UserOrder> driverOrder = findCurrentUserOrder(driver.get());

                    driverOrder.ifPresent(order -> processOrder(status, driverOrder.get()));
                }
            }
        }

        return requestFactory.createRedirectResponse(PagePaths.INCOMING_ORDER.getCommand());
    }

    private void processOrder(String status, UserOrder driverOrder) {
        switch (OrderStatus.getStatusByName(status)) {
        case CANCELLED:
            cancelOrder(driverOrder);
            break;
        case IN_PROCESS:
            takeOrder(driverOrder);
            break;
        }
    }

    private void cancelOrder(UserOrder userOrder) {
        orderService.update(userOrder.withStatus(OrderStatus.CANCELLED));
        driverService.update(userOrder.getDriver().withDriverStatus(DriverStatus.FREE));
    }

    private void takeOrder(UserOrder userOrder) {
        orderService.update(userOrder.withStatus(OrderStatus.IN_PROCESS));
        driverService.update(userOrder.getDriver().withDriverStatus(DriverStatus.BUSY));
    }

    private Optional<UserOrder> findCurrentUserOrder(Driver driver) {
        return orderService
                .findByDriver(driver)
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .findFirst();
    }

    public static DriverProcessOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DriverProcessOrderCommand INSTANCE = new DriverProcessOrderCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(OrderService.class),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class));
    }

}
