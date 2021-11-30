package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetUsersByAmountOfOrdersCommand extends CommonCommand {

    private static final int ZERO_VALUE = 0;
    private final UserService userService;
    private final UserOrderService userOrderService;

    public GetUsersByAmountOfOrdersCommand(RequestFactory requestFactory,
                                           UserService userService,
                                           UserOrderService userOrderService) {
        super(requestFactory);
        this.userService = userService;
        this.userOrderService = userOrderService;
    }

    private static class Holder {
        private static final GetUsersByAmountOfOrdersCommand INSTANCE = new GetUsersByAmountOfOrdersCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class)
        );
    }

    public static GetUsersByAmountOfOrdersCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Map<BuberUser, Integer> usersByOrderAmount = new HashMap<>();
        List<BuberUser> users = userService.findAll().stream()
                                           .filter(user -> user.getAccount().getRole() == Role.CLIENT)
                                           .collect(Collectors.toList());
        List<UserOrder> orders = new ArrayList<>(userOrderService.findAll());

        getUsersByOrderAmount(usersByOrderAmount, users, orders);

        return requestFactory.createJsonResponse(usersByOrderAmount, JsonResponseStatus.SUCCESS);
    }

    private void getUsersByOrderAmount(Map<BuberUser, Integer> usersByOrderAmount, List<BuberUser> users, List<UserOrder> orders) {
        for (BuberUser user : users) {
            for (UserOrder order : orders) {
                if (order.getClient().equals(user)) {
                    Integer amount = usersByOrderAmount.get(user);
                    usersByOrderAmount.put(user, amount == null? 1 : amount + 1);
                } else {
                    usersByOrderAmount.putIfAbsent(user, ZERO_VALUE);
                }
            }
            if (orders.isEmpty()) {
                usersByOrderAmount.put(user, ZERO_VALUE);
            }
        }
    }

}
