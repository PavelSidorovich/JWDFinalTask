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
        request.getParameter("id");
        Optional<BuberUser> buberUser = userService.findById(Long.parseLong(request.getParameter("id")));

        if (buberUser.isPresent()) {
            UserStatus status = buberUser.get().getStatus() == UserStatus.ACTIVE
                    ? UserStatus.BLOCKED
                    : UserStatus.ACTIVE;

            userService.update(buberUser.get().withStatus(status));
            return requestFactory.createJsonResponse(null, JsonResponseStatus.SUCCESS,
                                                     "User status was successfully updated!");
        }
        return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR,
                                                 "Error while updating user status");
    }

}
