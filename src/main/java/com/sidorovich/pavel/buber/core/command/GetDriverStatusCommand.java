package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class GetDriverStatusCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";

    private final DriverService driverService;

    private GetDriverStatusCommand(RequestFactory requestFactory,
                                      DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        Optional<Driver> driver = driverService.findById(id);

        if (driver.isPresent()) {
            return requestFactory.createJsonResponse(driver.get().getDriverStatus(), SUCCESS, null);
        }

        return requestFactory.createJsonResponse(null, ERROR, null);
    }

    public static GetDriverStatusCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final GetDriverStatusCommand INSTANCE = new GetDriverStatusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

}
