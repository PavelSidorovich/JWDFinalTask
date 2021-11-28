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

public class GetDriverCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String DRIVER_NOT_FOUND_MSG = "No such driver application!";

    private final DriverService driverService;

    private GetDriverCommand(RequestFactory requestFactory,
                             DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    private static class Holder {
        private static final GetDriverCommand INSTANCE = new GetDriverCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

    public static GetDriverCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        Optional<Driver> driver = driverService.findById(id);

        if (driver.isPresent()) {
            return requestFactory.createJsonResponse(driver.get(), SUCCESS, null);
        }

        return requestFactory.createJsonResponse(null, ERROR, DRIVER_NOT_FOUND_MSG);
    }

}
