package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class GetDriversCommand extends CommonCommand {

    private final DriverService driverService;

    private GetDriversCommand(RequestFactory requestFactory,
                              DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    private static class Holder {
        private static final GetDriversCommand INSTANCE = new GetDriversCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

    public static GetDriversCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Driver> drivers = new ArrayList<>(driverService.findAll());

        return requestFactory.createJsonResponse(drivers, JsonResponseStatus.SUCCESS);
    }

}
