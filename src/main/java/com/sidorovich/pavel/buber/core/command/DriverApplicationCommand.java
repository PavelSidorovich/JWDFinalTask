package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.util.ImageUploader;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.util.ImageUploaderImpl;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
import com.sidorovich.pavel.buber.core.validator.DriverRegisterValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class DriverApplicationCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(DriverApplicationCommand.class);

    private static final String BASE_NAME = "l10n.msg.error";
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
    private static final String IMAGES_FOLDER =
            "D:\\JavaWebDevelopment\\JWDFinalTask\\src\\main\\webapp\\images\\taxes";
    private static final String FILE_UPLOAD_ERROR_MSG = "Error while saving photo";

    private final BiValidator<Driver, String, Map<String, String>> validator;
    private final ResourceBundleExtractor resourceBundleExtractor;
    private final DriverService driverService;
    private final ImageUploader imageUploader;

    private DriverApplicationCommand(RequestFactory requestFactory,
                                     DriverService driverService,
                                     ImageUploader imageUploader,
                                     BiValidator<Driver, String, Map<String, String>> validator,
                                     ResourceBundleExtractor resourceBundleExtractor) {
        super(requestFactory);
        this.driverService = driverService;
        this.imageUploader = imageUploader;
        this.validator = validator;
        this.resourceBundleExtractor = resourceBundleExtractor;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        HashMap<String, String> errorsByMessages = new HashMap<>();

        try {
            String passwordRepeat = request.getParameter(PASSWORD_REPEAT_REQUEST_PARAM_NAME);
            String driverLicence = request.getParameter(DRIVER_LICENCE_PARAM_NAME);
            BuberUser user = buildUser(request);
            Taxi taxi = buildTaxi(request);
            Driver driver = new Driver(user, driverLicence, taxi, DriverStatus.PENDING);

            return processRegisterRequest(request, driver, passwordRepeat, errorsByMessages);
        } catch (Exception e) {
            LOG.error(e);
        }

        return requestFactory.createJsonResponse(errorsByMessages, ERROR, null);
    }

    private CommandResponse processRegisterRequest(CommandRequest request, Driver driver,
                                                   String passwordRepeat,
                                                   Map<String, String> errorsByMessages) {
        ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(request, BASE_NAME);

        errorsByMessages.putAll(validator.validate(driver, passwordRepeat, resourceBundle));
        if (errorsByMessages.isEmpty()) {
            try {
                Part photo = request.getPart(PHOTO_PATH_PARAM_NAME);
//                imageUploader.upload(request.getPart(PHOTO_PATH_PARAM_NAME), IMAGES_FOLDER);
                imageUploader.upload(photo, request.getContextPath() + "images\\taxes");
                driverService.save(driver);

                return requestFactory.createRedirectJsonResponse(PagePaths.DRIVER_APPLICATION_SUCCESS.getCommand());
            } catch (DuplicateKeyException e) {
                errorsByMessages.put(e.getAttribute(), e.getMessage());
            } catch (ServletException | IOException e) {
                errorsByMessages.put(PHOTO_PATH_PARAM_NAME, FILE_UPLOAD_ERROR_MSG);
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

    private Taxi buildTaxi(CommandRequest request) throws ServletException, IOException {
        String carBrand = request.getParameter(CAR_BRAND_PARAM_NAME);
        String carModel = request.getParameter(CAR_MODEL_PARAM_NAME);
        String licencePlate = request.getParameter(LICENCE_PLATE_PARAM_NAME);
        String photoFilename = request.getPart(PHOTO_PATH_PARAM_NAME).getSubmittedFileName();

        return new Taxi(
                carBrand, carModel, licencePlate, photoFilename,
                new Coordinates(new BigDecimal(LATITUDE_CENTER_MINSK),
                                new BigDecimal(LONGITUDE_CENTER_MINSK))
        );
    }

    public static DriverApplicationCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DriverApplicationCommand INSTANCE =
                new DriverApplicationCommand(
                        RequestFactoryImpl.getInstance(),
                        EntityServiceFactory.getInstance().serviceFor(DriverService.class),
                        ImageUploaderImpl.getInstance(),
                        DriverRegisterValidator.getInstance(),
                        ResourceBundleExtractorImpl.getInstance()
                );
    }

}
