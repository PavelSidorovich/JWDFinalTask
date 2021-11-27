package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class GetUsersCommand extends CommonCommand {

    private final UserService userService;

    private GetUsersCommand(RequestFactory requestFactory,
                            UserService userService) {
        super(requestFactory);
        this.userService = userService;
    }

    private static class Holder {
        private static final GetUsersCommand INSTANCE = new GetUsersCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class)
        );
    }

    public static GetUsersCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<BuberUser> users = userService.findAll().stream()
                                           .filter(user -> user.getAccount().getRole() != Role.ADMIN)
                                           .collect(Collectors.toList());

        return requestFactory.createJsonResponse(users, JsonResponseStatus.SUCCESS);
    }

}
