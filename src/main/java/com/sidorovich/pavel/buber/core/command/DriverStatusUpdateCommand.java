package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.mail.MailSender;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import java.util.Optional;

import static com.sidorovich.pavel.buber.core.controller.JsonResponseStatus.*;

public class DriverStatusUpdateCommand extends CommonCommand {

    private static final String ID_REQUEST_PARAM_NAME = "id";
    private static final String DRIVER_STATUS_REQUEST_PARAM_NAME = "driverStatus";
    private static final String COMMENT_REQUEST_PARAM_NAME = "comment";
    private static final String APPLICATION_REJECTED_MSG = "<p>Hello, %s %s!</p>" +
                                                           " <p>We are sorry that your driver application was rejected &#128577;</p>" +
                                                           " <p>Details: %s</p><p>---</p><p>Best wishes,</p><p>Buber.Taxi team &#128662;</p>";
    private static final String APPLICATION_APPROVED_MSG = "<p>Hello, %s %s!</p>" +
                                                           " <p>Your application was approved and you can take your first order! We are glad that you became part of our Buber.Taxi team! &#128512;</p>" +
                                                           " <p>Details: %s</p><p>---</p><p>Best wishes,</p><p>Buber.Taxi team &#128662;</p>";
    private static final String APPLICATION_WAS_APPROVED_SUBJECT = "Application was approved";
    private static final String APPLICATION_WAS_REJECTED_SUBJECT = "Application was rejected";
    private static final String EMPTY_STRING = "";

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
            final Driver taxiDriver = driver.get();

            if (taxiDriver.getDriverStatus() == DriverStatus.PENDING) {
                sendResponseToDriverEmail(request, driverStatus, taxiDriver);
            }
            driverService.update(taxiDriver.withDriverStatus(driverStatus));

            return requestFactory.createJsonResponse(taxiDriver, SUCCESS, null);
        }

        return requestFactory.createJsonResponse(null, ERROR, null);
    }

    private void sendResponseToDriverEmail(CommandRequest request, DriverStatus driverStatus, Driver driver) {
        String driverEmail = driver.getUser().getEmail().orElse(EMPTY_STRING);
        String emailSubject = EMPTY_STRING;
        String emailMessage = EMPTY_STRING;
        String adminComment = request.getParameter(COMMENT_REQUEST_PARAM_NAME);

        if (driverStatus == DriverStatus.REJECTED) {
            emailSubject = APPLICATION_WAS_REJECTED_SUBJECT;
            emailMessage = formatMailMessage(APPLICATION_REJECTED_MSG, driver, adminComment);
        } else if (driverStatus == DriverStatus.BUSY) {
            emailSubject = APPLICATION_WAS_APPROVED_SUBJECT;
            emailMessage = formatMailMessage(APPLICATION_APPROVED_MSG, driver, adminComment);
        }
        new MailSender(driverEmail, emailSubject, emailMessage).send();
    }

    private String formatMailMessage(String message, Driver driver, String comment) {
        final BuberUser user = driver.getUser();

        return String.format(message, user.getFirstName(), user.getLastName(), comment);
    }

}
