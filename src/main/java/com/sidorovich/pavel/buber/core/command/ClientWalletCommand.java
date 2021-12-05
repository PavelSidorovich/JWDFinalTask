package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sidorovich.pavel.buber.api.model.OrderStatus.*;

public class ClientWalletCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String CASH_ATTR_PARAM_NAME = "cash";
    private static final String DEBITS_ATTR_PARAM_NAME = "debits";
    private static final int LAST_OPERATIONS_AMOUNT = 5;

    private final UserOrderService orderService;
    private final UserService userService;

    private ClientWalletCommand(RequestFactory requestFactory,
                                UserOrderService orderService,
                                UserService userService) {
        super(requestFactory);
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists()) {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME).orElseGet(null);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            if (user.isPresent()) {
                List<BigDecimal> debits = getDebits(user.get());

                request.addAttributeToJsp(CASH_ATTR_PARAM_NAME, user.get().getCash());
                request.addAttributeToJsp(DEBITS_ATTR_PARAM_NAME, debits);
            }
        }

        return requestFactory.createForwardResponse(PagePaths.CLIENT_WALLET.getJspPath());
    }

    private List<BigDecimal> getDebits(BuberUser user) {
        return orderService
                .findByClient(user)
                .stream()
                .filter(order -> order.getStatus() == COMPLETED)
                .sorted(Comparator.comparing(o -> o.getId().orElse(-1L)))
                .map(UserOrder::getPrice)
                .limit(LAST_OPERATIONS_AMOUNT)
                .collect(Collectors.toList());
    }

    public static ClientWalletCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ClientWalletCommand INSTANCE = new ClientWalletCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserOrderService.class),
                EntityServiceFactory.getInstance().serviceFor(UserService.class));
    }

}
