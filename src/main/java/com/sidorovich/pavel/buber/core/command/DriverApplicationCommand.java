package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.service.ImageUploader;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.ImageUploaderImpl;
import com.sidorovich.pavel.buber.core.validator.DriverRegisterValidator;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class DriverApplicationCommand extends CommonCommand {

    private static final String F_NAME_REQUEST_PARAM_NAME = "fName";
    private static final String L_NAME_REQUEST_PARAM_NAME = "lName";
    private static final String PHONE_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_REQUEST_PARAM_NAME = "passwordRepeat";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String DRIVER_LICENCE_PARAM_NAME = "drivingLicence";
    private static final String CAR_BRAND_PARAM_NAME = "brand";
    private static final String CAR_MODEL_PARAM_NAME = "model";
    private static final String LICENCE_PLATE_PARAM_NAME = "licencePlate";
    private static final String PHOTO_PATH_PARAM_NAME = "carPhoto";
    private static final String LATITUDE_CENTER_MINSK = "53.90154429809449";
    private static final String LONGITUDE_CENTER_MINSK = "27.56100654602051";
    private static final String EMPTY_STRING = "";
    private static final String IMAGES_FOLDER =
            "D:\\JavaWebDevelopment\\JWDFinalTask\\src\\main\\webapp\\images\\taxes";

    private final DriverService driverService;
    private final ImageUploader imageUploader;
    private final BiValidator<Driver, String, Map<String, String>> validator;

    private DriverApplicationCommand(RequestFactory requestFactory,
                                     DriverService driverService,
                                     ImageUploader imageUploader,
                                     BiValidator<Driver, String, Map<String, String>> validator) {
        super(requestFactory);
        this.driverService = driverService;
        this.imageUploader = imageUploader;
        this.validator = validator;
    }

    private static class Holder {
        private static final DriverApplicationCommand INSTANCE =
                new DriverApplicationCommand(
                        RequestFactoryImpl.getInstance(),
                        EntityServiceFactory.getInstance().serviceFor(DriverService.class),
                        ImageUploaderImpl.getInstance(),
                        DriverRegisterValidator.getInstance()
                );
    }

    public static DriverApplicationCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String passwordRepeat = request.getParameter(PASSWORD_REPEAT_REQUEST_PARAM_NAME);
        String driverLicence = request.getParameter(DRIVER_LICENCE_PARAM_NAME);
        BuberUser user = buildUser(request);
        Taxi taxi = buildTaxi(request);
        Driver driver = new Driver(user, driverLicence, taxi, DriverStatus.PENDING);

        return processRegisterRequest(driver, passwordRepeat);
    }

    private CommandResponse processRegisterRequest(Driver driver, String passwordRepeat) {
        Map<String, String> errorsByMessages =
                new HashMap<>(validator.validate(driver, passwordRepeat));

        if (errorsByMessages.isEmpty()) {
            try {
                driverService.save(driver);

                return requestFactory.createRedirectJsonResponse(PagePaths.DRIVER_APPLICATION_SUCCESS.getCommand());
            } catch (DuplicateKeyException e) {
                errorsByMessages.put(e.getAttribute(), e.getMessage());
            }
        }

        return requestFactory.createJsonResponse(errorsByMessages, ERROR, null);
    }

    private BuberUser buildUser(CommandRequest request) {
        String fName = request.getParameter(F_NAME_REQUEST_PARAM_NAME);
        String lName = request.getParameter(L_NAME_REQUEST_PARAM_NAME);
        String phone = request.getParameter(PHONE_REQUEST_PARAM_NAME);
        String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);

        return BuberUser.with()
                        .account(new Account(phone, password, Role.DRIVER))
                        .status(UserStatus.ACTIVE)
                        .cash(BigDecimal.ZERO)
                        .email(email)
                        .firstName(fName)
                        .lastName(lName)
                        .build();
    }

    private Taxi buildTaxi(CommandRequest request) {
        String carBrand = request.getParameter(CAR_BRAND_PARAM_NAME);
        String carModel = request.getParameter(CAR_MODEL_PARAM_NAME);
        String licencePlate = request.getParameter(LICENCE_PLATE_PARAM_NAME);
        String photoPath;

        try {
            photoPath = imageUploader.upload(request.getPart(PHOTO_PATH_PARAM_NAME), IMAGES_FOLDER);
        } catch (ServletException | IOException e) {
            photoPath = EMPTY_STRING;
        }

        return new Taxi(
                carBrand, carModel, licencePlate, photoPath,
                new Coordinates(new BigDecimal(LATITUDE_CENTER_MINSK),
                                new BigDecimal(LONGITUDE_CENTER_MINSK))
        );
    }

}
