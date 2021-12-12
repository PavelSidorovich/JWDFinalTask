package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
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

public class CancelOrderCommand extends CommonCommand {

    public static final String USER_SESSION_PARAM_NAME = "user";

    private final UserOrderService orderService;
    private final UserService userService;
    private final DriverService driverService;

    private CancelOrderCommand(RequestFactory requestFactory,
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
        Optional<Object> optAccount = request.retrieveFromSession(USER_SESSION_PARAM_NAME);

        if (optAccount.isPresent()) {
            Optional<BuberUser> user = userService.findByPhone(((Account) optAccount.get()).getPhone());

            if (user.isPresent()) {
                Optional<UserOrder> userOrder = findCurrentUserOrder(user.get());

                userOrder.ifPresent(this::cancelOrder);
            }
        }

        return requestFactory.createRedirectResponse(PagePaths.CLIENT_ORDER.getCommand());
    }

    private void cancelOrder(UserOrder userOrder) {
        orderService.update(userOrder.withStatus(OrderStatus.CANCELLED));
        if (userOrder.getDriver() != null) {
            driverService.update(userOrder.getDriver().withDriverStatus(DriverStatus.FREE));
        }
    }

    private Optional<UserOrder> findCurrentUserOrder(BuberUser user) {
        return orderService
                .findByClient(user)
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .findFirst();
    }

    public static CancelOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CancelOrderCommand INSTANCE = new CancelOrderCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class));
    }

}
