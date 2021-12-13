package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

import static com.sidorovich.pavel.buber.api.model.DriverStatus.*;
import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class ToggleDriverStatusCommand extends CommonCommand {

    private static final String IS_RESTING_PARAM_NAME = "isResting";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final long INVALID_INDEX = 1L;

    private final DriverService driverService;

    private ToggleDriverStatusCommand(RequestFactory requestFactory,
                                      DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> optAccount = request.retrieveFromSession(USER_SESSION_PARAM_NAME);
        boolean isResting = Boolean.parseBoolean(request.getParameter(IS_RESTING_PARAM_NAME));

        if (optAccount.isPresent()) {
            Optional<Driver> driver =
                    driverService.findById(((Account) optAccount.get()).getId().orElse(INVALID_INDEX));

            if (driver.isPresent()) {
                final Driver taxiDriver = driver.get();
                final DriverStatus status = isResting? FREE : REST;

                if (taxiDriver.getDriverStatus() == FREE
                    || taxiDriver.getDriverStatus() == REST) {
                    driverService.update(taxiDriver.withDriverStatus(status));
                    return requestFactory.createJsonResponse(taxiDriver, SUCCESS, null);
                }
            }
        }
        return requestFactory.createJsonResponse(null, ERROR, null);
    }

    public static ToggleDriverStatusCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ToggleDriverStatusCommand INSTANCE = new ToggleDriverStatusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

}
