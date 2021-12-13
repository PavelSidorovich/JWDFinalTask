package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.OrderService;

import java.util.Optional;

import static com.sidorovich.pavel.buber.api.model.OrderStatus.*;

public class IncomingOrderCommand extends CommonCommand {

    private static final String ORDER_ATTR_PARAM_NAME = "order";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String DRIVER_STATUS_ATTR_PARAM_NAME = "driverStatus";

    private final DriverService driverService;
    private final OrderService orderService;

    private IncomingOrderCommand(RequestFactory requestFactory,
                                 DriverService driverService,
                                 OrderService orderService) {
        super(requestFactory);
        this.driverService = driverService;
        this.orderService = orderService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<Driver> driver = driverService.findById(account.getId().orElse(-1L));
            Optional<UserOrder> driverIncomingOrder = Optional.empty();

            if (driver.isPresent()) {
                driverIncomingOrder = orderService
                        .findByDriver(driver.get())
                        .stream()
                        .filter(order -> order.getStatus() == NEW || order.getStatus() == IN_PROCESS)
                        .findFirst();
                request.addAttributeToJsp(DRIVER_STATUS_ATTR_PARAM_NAME, driver.get().getDriverStatus());
            }
            driverIncomingOrder.ifPresent(order -> request.addAttributeToJsp(ORDER_ATTR_PARAM_NAME, order));
        }

        return requestFactory.createForwardResponse(PagePaths.INCOMING_ORDER.getJspPath());
    }

    public static IncomingOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final IncomingOrderCommand INSTANCE = new IncomingOrderCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class),
                EntityServiceFactory.getInstance().serviceFor(OrderService.class));
    }

}
