package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.service.ImageUploader;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.ImageUploaderImpl;
import com.sidorovich.pavel.buber.core.service.TaxiService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

public class EditDriverInfoCommand extends CommonCommand {

    private static final String IMAGES_FOLDER =
            "D:\\JavaWebDevelopment\\JWDFinalTask\\src\\main\\webapp\\images\\taxes";
    private static final String PHOTO_PATH_PARAM_NAME = "carPhoto";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final long INVALID_INDEX = -1L;
    private static final String CAR_BRAND_REQUEST_PARAM_NAME = "carBrand";
    private static final String CAR_MODEL_REQUEST_PARAM_NAME = "carModel";
    private static final String CAR_LICENCE_PLATE_REQUEST_PARAM_NAME = "carLicencePlate";
    private static final String DRIVING_LICENCE_PARAM_NAME = "drivingLicenceSerial";

    private final DriverService driverService;
    private final TaxiService taxiService;
    private final ImageUploader imageUploader;

    private EditDriverInfoCommand(RequestFactory requestFactory,
                                  DriverService driverService,
                                  TaxiService taxiService,
                                  ImageUploader imageUploader) {
        super(requestFactory);
        this.driverService = driverService;
        this.taxiService = taxiService;
        this.imageUploader = imageUploader;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> account = request.retrieveFromSession(USER_SESSION_PARAM_NAME);

        if (account.isPresent()) {
            Optional<Driver> driver = driverService.findById(
                    ((Account) account.get()).getId().orElse(INVALID_INDEX)
            );

            driver.ifPresent(value -> updateDriver(request, value));
        }

        return requestFactory.createRedirectResponse(PagePaths.MY_TAXI.getCommand());
    }

    private void updateDriver(CommandRequest request, Driver driver) {
        final Taxi taxi = driver.getTaxi();
        final Taxi updatedTaxi = getUpdatedTaxi(request, taxi);
        final Driver updatedDriver = getUpdatedDriver(request, driver, updatedTaxi);

        taxiService.update(updatedTaxi);
        driverService.update(updatedDriver);
    }

    private Driver getUpdatedDriver(CommandRequest request, Driver driverToUpdate, Taxi updatedTaxi) {
        String drivingLicence = request.getParameter(DRIVING_LICENCE_PARAM_NAME);

        return new Driver(driverToUpdate.getUser(), drivingLicence, updatedTaxi, DriverStatus.PENDING);
    }

    private Taxi getUpdatedTaxi(CommandRequest request, Taxi taxi) {
        String carModel = request.getParameter(CAR_MODEL_REQUEST_PARAM_NAME);
        String carLicencePlate = request.getParameter(
                CAR_LICENCE_PLATE_REQUEST_PARAM_NAME);
        String carBrand = request.getParameter(CAR_BRAND_REQUEST_PARAM_NAME);
        String photoPath;

        try {
            photoPath = imageUploader.upload(request.getPart(PHOTO_PATH_PARAM_NAME), IMAGES_FOLDER);
        } catch (ServletException | IOException e) {
            photoPath = taxi.getPhotoFilepath();
        }

        return new Taxi(taxi.getId().orElse(INVALID_INDEX), carBrand,
                        carModel, carLicencePlate, photoPath.isEmpty()? taxi.getPhotoFilepath() : photoPath,
                        taxi.getLastCoordinates());
    }

    public static EditDriverInfoCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EditDriverInfoCommand INSTANCE = new EditDriverInfoCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(DriverService.class),
                EntityServiceFactory.getInstance().serviceFor(TaxiService.class),
                ImageUploaderImpl.getInstance());
    }

}
