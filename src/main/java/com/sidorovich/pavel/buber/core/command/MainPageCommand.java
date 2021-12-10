package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainPageCommand extends CommonCommand {

    private static final int AMOUNT_OF_CAR_PHOTOS_IN_CAROUSEL = 10;
    private static final String CAR_PHOTOS_ATTR_PARAM_NAME = "carPhotoPaths";

    private final DriverService driverService;

    private MainPageCommand(RequestFactory requestFactory,
                            DriverService driverService) {
        super(requestFactory);
        this.driverService = driverService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<String> carPhotoPaths = getCarPhotoPathOfActiveDrivers();

        Collections.shuffle(carPhotoPaths);
        List<String> toShowOnMainPage = carPhotoPaths.stream()
                                                     .limit(AMOUNT_OF_CAR_PHOTOS_IN_CAROUSEL)
                                                     .collect(Collectors.toList());

        request.addAttributeToJsp(CAR_PHOTOS_ATTR_PARAM_NAME, toShowOnMainPage);

        return requestFactory.createForwardResponse(PagePaths.MAIN.getJspPath());
    }

    private List<String> getCarPhotoPathOfActiveDrivers() {
        return driverService.findAll().stream()
                            .filter(driver -> driver.getDriverStatus() != DriverStatus.PENDING
                                              && driver.getDriverStatus() != DriverStatus.REJECTED)
                            .map(driver -> driver.getTaxi().getPhotoFilepath())
                            .distinct()
                            .collect(Collectors.toList());
    }

    public static MainPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MainPageCommand INSTANCE = new MainPageCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class)
        );
    }

}
