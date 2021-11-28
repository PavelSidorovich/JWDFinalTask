package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.Optional;

public class BlockUserCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String USER_UPDATED_MSG = "User status was successfully updated!";
    private static final String ERROR_USER_UPDATING_MSG = "Error while updating user status";

    private final UserService userService;

    private BlockUserCommand(RequestFactory requestFactory,
                             UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    private static class Holder {
        private static final BlockUserCommand INSTANCE = new BlockUserCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class)
        );
    }

    public static BlockUserCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        Optional<BuberUser> buberUser = userService.findById(id);

        if (buberUser.isPresent()) {
            UserStatus status = buberUser.get().getStatus() == UserStatus.ACTIVE
                    ? UserStatus.BLOCKED
                    : UserStatus.ACTIVE;

            userService.update(buberUser.get().withStatus(status));
            return requestFactory.createJsonResponse(null, JsonResponseStatus.SUCCESS,
                                                     USER_UPDATED_MSG);
        }
        return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR,
                                                 ERROR_USER_UPDATING_MSG);
    }

}
