package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

public class TaxiInfoCommand extends CommonCommand {

    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String DRIVER_ATTR_NAME = "driver";

    private final DriverService driverService;

    private TaxiInfoCommand(RequestFactory requestFactory,
                            DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> account = request.retrieveFromSession(USER_SESSION_PARAM_NAME);

        if (account.isPresent()) {
            Optional<Driver> driver = driverService.findById(
                    ((Account) account.get()).getId().orElse(-1L)
            );

            driver.ifPresent(value -> request.addAttributeToJsp(DRIVER_ATTR_NAME, value));
        }

        return requestFactory.createForwardResponse(PagePaths.MY_TAXI.getJspPath());
    }

    public static TaxiInfoCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TaxiInfoCommand INSTANCE = new TaxiInfoCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );

    }

}
