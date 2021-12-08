package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

public class GetMyBonusesCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String USER_BONUSES_ATTR_PARAM_NAME = "userBonuses";
    private static final long INVALID_INDEX = -1L;

    private final BonusService bonusService;

    private GetMyBonusesCommand(RequestFactory requestFactory,
                                BonusService bonusService) {
        super(requestFactory);
        this.bonusService = bonusService;
    }

    private static class Holder {
        private static final GetMyBonusesCommand INSTANCE = new GetMyBonusesCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class)
        );
    }

    public static GetMyBonusesCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> account = request.retrieveFromSession(USER_SESSION_PARAM_NAME);

        account.ifPresent(
                acc -> request.addAttributeToJsp(
                        USER_BONUSES_ATTR_PARAM_NAME,
                        bonusService.findBonusesByUserId(((Account) acc).getId().orElse(INVALID_INDEX))
                )
        );

        return requestFactory.createForwardResponse(PagePaths.MY_BONUSES.getJspPath());
    }

}
