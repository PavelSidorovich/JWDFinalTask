package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.Optional;

public class CancelOrderCommand extends CommonCommand {

    public static final String USER_SESSION_PARAM_NAME = "user";

    private final UserOrderService orderService;
    private final UserService userService;

    private CancelOrderCommand(RequestFactory requestFactory,
                               UserOrderService orderService,
                               UserService userService) {
        super(requestFactory);
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElse(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());
            Optional<UserOrder> userOrder = Optional.empty();

            if (user.isPresent()) {
                userOrder = orderService
                        .findByClient(user.get())
                        .stream()
                        .filter(order -> order.getStatus() == OrderStatus.NEW)
                        .findFirst();
            }
            userOrder.ifPresent(order -> orderService.update(order.withStatus(OrderStatus.CANCELLED)));

        }
        return requestFactory.createRedirectResponse(PagePaths.CLIENT_ORDER.getCommand());
    }

    public static CancelOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CancelOrderCommand INSTANCE = new CancelOrderCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(UserService.class)
        );
    }

}
