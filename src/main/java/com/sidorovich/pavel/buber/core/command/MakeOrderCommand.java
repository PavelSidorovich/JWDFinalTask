package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sidorovich.pavel.buber.api.model.OrderStatus.*;

public class MakeOrderCommand extends CommonCommand {

    private static final String NO_FREE_DRIVERS_MSG = "No free drivers. Please, wait for few minutes :(";
    private static final String ORDER_ATTR_PARAM_NAME = "order";
    private static final String BONUSES_ATTR_PARAM_NAME = "bonuses";
    private static final String TAXI_ERROR_ATTR_PARAM_NAME = "taxiError";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String TAXIS_REQUEST_ATTR_NAME = "taxis";

    private final DriverService driverService;
    private final UserOrderService orderService;
    private final UserService userService;
    private final BonusService bonusService;

    private MakeOrderCommand(RequestFactory requestFactory,
                             DriverService driverService,
                             UserOrderService orderService,
                             UserService userService,
                             BonusService bonusService) {
        super(requestFactory);
        this.driverService = driverService;
        this.orderService = orderService;
        this.userService = userService;
        this.bonusService = bonusService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());
            Optional<UserOrder> userOrder = Optional.empty();

            if (user.isPresent()) {
                userOrder = orderService
                        .findByClient(user.get())
                        .stream()
                        .filter(order -> order.getStatus() == NEW || order.getStatus() == IN_PROCESS)
                        .findFirst();
                addBonusesToJsp(request, user.get());
                addTaxisToJsp(request);
            }
            userOrder.ifPresent(order -> request.addAttributeToJsp(ORDER_ATTR_PARAM_NAME, order));
        }

        return requestFactory.createForwardResponse(PagePaths.CLIENT_ORDER.getJspPath());
    }

    private void addBonusesToJsp(CommandRequest request, BuberUser user) {
        List<Bonus> bonuses = bonusService.findBonusesByUserId(user.getId().orElse(-1L));

        request.addAttributeToJsp(BONUSES_ATTR_PARAM_NAME, bonuses);
    }

    private void addTaxisToJsp(CommandRequest request) {
        List<Taxi> taxis = driverService.findAll().stream()
                                        .filter(driver -> driver.getDriverStatus() == DriverStatus.FREE)
                                        .map(Driver::getTaxi)
                                        .collect(Collectors.toList());

        if (taxis.isEmpty()) {
            request.addAttributeToJsp(TAXI_ERROR_ATTR_PARAM_NAME, NO_FREE_DRIVERS_MSG);
        }
        request.addAttributeToJsp(TAXIS_REQUEST_ATTR_NAME, taxis);
    }

    public static MakeOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MakeOrderCommand INSTANCE = new MakeOrderCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class));
    }

}
