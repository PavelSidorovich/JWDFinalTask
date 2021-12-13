package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;

import java.util.Optional;
import java.util.ResourceBundle;

public class BlockUserCommand extends CommonCommand {

    private static final String ERROR_BASE_NAME = "l10n.msg.error";
    private static final String SUCCESS_BASE_NAME = "l10n.msg.success";
    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String USER_UPDATED_KEY = "msg.userStatusUpdate";
    private static final String ERROR_USER_UPDATING_KEY = "msg.userStatusUpdate";

    private final UserService userService;
    private final ResourceBundleExtractor bundleExtractor;

    private BlockUserCommand(RequestFactory requestFactory,
                             UserService userService,
                             ResourceBundleExtractor bundleExtractor) {
        super(requestFactory);
        this.userService = userService;
        this.bundleExtractor = bundleExtractor;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        Optional<BuberUser> buberUser = userService.findById(id);
        ResourceBundle errorBundle = bundleExtractor.extractResourceBundle(request, ERROR_BASE_NAME);
        ResourceBundle successBundle = bundleExtractor.extractResourceBundle(request, SUCCESS_BASE_NAME);

        if (buberUser.isPresent()) {
            UserStatus status = buberUser.get().getStatus() == UserStatus.ACTIVE
                    ? UserStatus.BLOCKED
                    : UserStatus.ACTIVE;

            userService.update(buberUser.get().withStatus(status));
            return requestFactory.createJsonResponse(null, JsonResponseStatus.SUCCESS,
                                                     successBundle.getString(USER_UPDATED_KEY));
        }
        return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR,
                                                 errorBundle.getString(ERROR_USER_UPDATING_KEY));
    }

    public static BlockUserCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BlockUserCommand INSTANCE = new BlockUserCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
