package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class DriverStatusUpdateCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String DRIVER_STATUS_REQUEST_PARAM_NAME = "driverStatus";

    private final DriverService driverService;

    private DriverStatusUpdateCommand(RequestFactory requestFactory,
                                      DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    private static class Holder {
        private static final DriverStatusUpdateCommand INSTANCE = new DriverStatusUpdateCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

    public static DriverStatusUpdateCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        long id = Long.parseLong(request.getParameter(ID_REQUEST_PARAM_NAME));
        DriverStatus driverStatus = DriverStatus.getStatusByName(
                request.getParameter(DRIVER_STATUS_REQUEST_PARAM_NAME)
        );
        Optional<Driver> driver = driverService.findById(id);

        if (driver.isPresent()) {
            driverService.update(driver.get().withDriverStatus(driverStatus));

            return requestFactory.createJsonResponse(driver.get(), SUCCESS, null);
        }

        return requestFactory.createJsonResponse(null, ERROR, null);
    }

}
