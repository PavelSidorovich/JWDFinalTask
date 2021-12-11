package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.service.ImageUploader;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.ImageUploaderImpl;
import com.sidorovich.pavel.buber.core.service.TaxiService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
import com.sidorovich.pavel.buber.core.validator.DrivingLicenceValidator;
import com.sidorovich.pavel.buber.core.validator.TaxiValidator;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditDriverInfoCommand extends CommonCommand {

    private static final String BASE_NAME = "l10n.msg.error";
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
    private final Validator<String, Map<String, String>> drivingLicenceValidator;
    private final Validator<Taxi, Map<String, String>> taxiValidator;
    private final ResourceBundleExtractor resourceBundleExtractor;

    private EditDriverInfoCommand(RequestFactory requestFactory,
                                  DriverService driverService,
                                  TaxiService taxiService,
                                  ImageUploader imageUploader,
                                  Validator<String, Map<String, String>> drivingLicenceValidator,
                                  Validator<Taxi, Map<String, String>> taxiValidator,
                                  ResourceBundleExtractor resourceBundleExtractor) {
        super(requestFactory);
        this.driverService = driverService;
        this.taxiService = taxiService;
        this.imageUploader = imageUploader;
        this.drivingLicenceValidator = drivingLicenceValidator;
        this.taxiValidator = taxiValidator;
        this.resourceBundleExtractor = resourceBundleExtractor;
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
        ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(request, BASE_NAME);

        if (drivingLicenceValidator.validate(updatedDriver.getDrivingLicence(), resourceBundle).isEmpty()
            && taxiValidator.validate(updatedTaxi, resourceBundle).isEmpty()) {
            taxiService.update(updatedTaxi);
            driverService.update(updatedDriver);
        }
    }

    private Driver getUpdatedDriver(CommandRequest request, Driver driverToUpdate, Taxi updatedTaxi) {
        final String drivingLicence = request.getParameter(DRIVING_LICENCE_PARAM_NAME);

        return new Driver(driverToUpdate.getUser(), drivingLicence, updatedTaxi, DriverStatus.PENDING);
    }

    private Taxi getUpdatedTaxi(CommandRequest request, Taxi taxi) {
        final String carModel = request.getParameter(CAR_MODEL_REQUEST_PARAM_NAME);
        final String carLicencePlate = request.getParameter(
                CAR_LICENCE_PLATE_REQUEST_PARAM_NAME);
        final String carBrand = request.getParameter(CAR_BRAND_REQUEST_PARAM_NAME);
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
                ImageUploaderImpl.getInstance(),
                DrivingLicenceValidator.getInstance(),
                TaxiValidator.getInstance(),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
