package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.TaxiService;

import java.util.Optional;

public class GetTaxiPhotoPathCommand extends CommonCommand {

    private static final String LICENCE_PLATE_REQUEST_PARAM_NAME = "licencePlate";
    private static final String PHOTO_PATH_ATTR_NAME = "photoPath";
    private static final String TAXIS_IMAGES_FOLDER_PATH = "../images/taxes/";

    private final TaxiService taxiService;

    private GetTaxiPhotoPathCommand(RequestFactory requestFactory, TaxiService taxiService) {
        super(requestFactory);
        this.taxiService = taxiService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String licencePlate = request.getParameter(LICENCE_PLATE_REQUEST_PARAM_NAME);

        if (!licencePlate.isEmpty()) {
            Optional<Taxi> taxiByLicencePlate = taxiService.findByLicencePlate(licencePlate);

            taxiByLicencePlate.ifPresent(
                    taxi -> request.addAttributeToJsp(PHOTO_PATH_ATTR_NAME, taxi.getPhotoFilepath()));
            if (taxiByLicencePlate.isPresent()) {
                return requestFactory.createJsonResponse(TAXIS_IMAGES_FOLDER_PATH
                                                         + taxiByLicencePlate.get().getPhotoFilepath(),
                                                         JsonResponseStatus.SUCCESS);
            }
        }

        return requestFactory.createJsonResponse(null, JsonResponseStatus.SUCCESS);
    }

    private static class Holder {
        private static final GetTaxiPhotoPathCommand INSTANCE = new GetTaxiPhotoPathCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(TaxiService.class)
        );
    }

    public static GetTaxiPhotoPathCommand getInstance() {
        return Holder.INSTANCE;
    }

}
