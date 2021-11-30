package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

public class DeleteBonusCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String DELETION_OPERATION_FAIL = "Bonus deletion operation fail!";
    private static final String SUCCESSFUL_BONUS_DELETION = "Successful bonus deletion!";

    private final BonusService bonusService;

    private DeleteBonusCommand(RequestFactory requestFactory,
                               BonusService bonusService) {
        super(requestFactory);
        this.bonusService = bonusService;
    }

    private static class Holder {
        private static final DeleteBonusCommand INSTANCE = new DeleteBonusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class)
        );
    }

    public static DeleteBonusCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));

        JsonResponseStatus status;
        String msg;

        if (bonusService.delete(id)) {
            status = JsonResponseStatus.SUCCESS;
            msg = SUCCESSFUL_BONUS_DELETION;
        } else {
            status = JsonResponseStatus.ERROR;
            msg = DELETION_OPERATION_FAIL;
        }

        return requestFactory.createJsonResponse(null, status, msg);
    }

}
