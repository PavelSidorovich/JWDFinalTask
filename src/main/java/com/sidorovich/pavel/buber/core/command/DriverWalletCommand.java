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
import com.sidorovich.pavel.buber.core.service.UserOrderService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sidorovich.pavel.buber.api.model.OrderStatus.*;

public class DriverWalletCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String CASH_ATTR_PARAM_NAME = "cash";
    private static final String CREDITS_ATTR_PARAM_NAME = "credits";
    private static final int LAST_OPERATIONS_AMOUNT = 5;

    private final UserOrderService orderService;
    private final DriverService driverService;

    private DriverWalletCommand(RequestFactory requestFactory,
                                UserOrderService orderService,
                                DriverService driverService) {
        super(requestFactory);
        this.orderService = orderService;
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
        Optional<Driver> driver = driverService.findById(account.getId().orElse(-1L));

        if (driver.isPresent()) {
            List<BigDecimal> credits = getCredits(driver.get());

            request.addAttributeToJsp(CASH_ATTR_PARAM_NAME, driver.get().getUser().getCash());
            request.addAttributeToJsp(CREDITS_ATTR_PARAM_NAME, credits);
        }

        return requestFactory.createForwardResponse(PagePaths.DRIVER_WALLET.getJspPath());
    }

    private List<BigDecimal> getCredits(Driver driver) {
        return orderService
                .findByDriver(driver)
                .stream()
                .filter(order -> order.getStatus() == COMPLETED)
                .sorted(Comparator.comparing(o -> o.getId().orElse(-1L)))
                .map(UserOrder::getPrice)
                .limit(LAST_OPERATIONS_AMOUNT)
                .collect(Collectors.toList());
    }

    public static DriverWalletCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DriverWalletCommand INSTANCE = new DriverWalletCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

}
