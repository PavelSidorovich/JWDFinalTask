package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;

import java.util.ResourceBundle;

public class DeleteBonusCommand extends CommonCommand {

    private static final String ERROR_BASE_NAME = "l10n.msg.error";
    private static final String SUCCESS_BASE_NAME = "l10n.msg.success";
    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String DELETION_OPERATION_FAIL_KEY = "msg.bonusDeletion";
    private static final String SUCCESSFUL_BONUS_DELETION_KEY = "msg.bonusDeleted";

    private final BonusService bonusService;
    private final ResourceBundleExtractor bundleExtractor;

    private DeleteBonusCommand(RequestFactory requestFactory,
                               BonusService bonusService,
                               ResourceBundleExtractor bundleExtractor) {
        super(requestFactory);
        this.bonusService = bonusService;
        this.bundleExtractor = bundleExtractor;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        JsonResponseStatus status;
        String msg;

        if (bonusService.delete(id)) {
            ResourceBundle bundle = bundleExtractor.extractResourceBundle(request, SUCCESS_BASE_NAME);
            status = JsonResponseStatus.SUCCESS;
            msg = bundle.getString(SUCCESSFUL_BONUS_DELETION_KEY);
        } else {
            ResourceBundle bundle = bundleExtractor.extractResourceBundle(request, ERROR_BASE_NAME);
            status = JsonResponseStatus.ERROR;
            msg = bundle.getString(DELETION_OPERATION_FAIL_KEY);
        }

        return requestFactory.createJsonResponse(null, status, msg);
    }

    public static DeleteBonusCommand getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder {
        private static final DeleteBonusCommand INSTANCE = new DeleteBonusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
