package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.exception.EmptySessionException;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class TopUpBalanceCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(TopUpBalanceCommand.class);

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String CASH_REQUEST_PARAM_NAME = "cash";

    private final UserService userService;

    private TopUpBalanceCommand(RequestFactory requestFactory,
                                UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            topUpBalance(request);
        } catch (NumberFormatException e) {
            LOG.error(e);
        }

        return requestFactory.createRedirectResponse(PagePaths.CLIENT_WALLET.getCommand());
    }

    private void topUpBalance(CommandRequest request) {
        try {
            BigDecimal cash = new BigDecimal(request.getParameter(CASH_REQUEST_PARAM_NAME));
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME)
                                               .orElseThrow(EmptySessionException::new);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            if (user.isPresent()) {
                BuberUser client = user.get();
                BigDecimal currentCash = client.getCash();

                userService.update(client.withCash(currentCash.add(cash)));
            }
        } catch (EmptySessionException e) {
            LOG.error(e);
        }
    }

    public static TopUpBalanceCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TopUpBalanceCommand INSTANCE = new TopUpBalanceCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class));
    }

}
