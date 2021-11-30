package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class GetBonusesCommand extends CommonCommand {

    private final BonusService bonusService;

    private GetBonusesCommand(RequestFactory requestFactory,
                              BonusService bonusService) {
        super(requestFactory);
        this.bonusService = bonusService;
    }

    private static class Holder {
        private static final GetBonusesCommand INSTANCE = new GetBonusesCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class)
        );
    }

    public static GetBonusesCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Bonus> bonuses = new ArrayList<>(bonusService.findAll());

        return requestFactory.createJsonResponse(bonuses, JsonResponseStatus.SUCCESS);
    }

}
