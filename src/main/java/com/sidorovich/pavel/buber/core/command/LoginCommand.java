package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.util.CoordinateRandomizer;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.AccountService;
import com.sidorovich.pavel.buber.core.util.CoordinateRandomizerImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;

import java.util.Optional;
import java.util.ResourceBundle;

public class LoginCommand extends CommonCommand {

    private static final String BASE_NAME = "l10n.msg.error";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "phone";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String INVALID_LOGIN_OR_PASS_KEY = "msg.invalidPassword";
    private static final String USER_BLOCKED_KEY = "msg.userBlocked";
    private static final String LONGITUDE_SESSION_ATTRIBUTE_NAME = "longitude";
    private static final String LATITUDE_SESSION_ATTRIBUTE_NAME = "latitude";

    private final CoordinateRandomizer randomizer;
    private final AccountService accountService;
    private final UserService userService;
    private final ResourceBundleExtractor bundleExtractor;

    private LoginCommand(RequestFactory requestFactory,
                         CoordinateRandomizer randomizer,
                         AccountService accountService, UserService userService,
                         ResourceBundleExtractor bundleExtractor) {
        super(requestFactory);
        this.randomizer = randomizer;
        this.accountService = accountService;
        this.userService = userService;
        this.bundleExtractor = bundleExtractor;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME).isPresent()) {
            request.clearSession(); // terminate current session
        }
        final Optional<Account> account = findAccount(request);
        final ResourceBundle bundle = bundleExtractor.extractResourceBundle(request, BASE_NAME);

        if (!account.isPresent()) {
            return createJsonWithMessage(bundle.getString(INVALID_LOGIN_OR_PASS_KEY));
        }
        final Account acc = account.get();
        final Optional<BuberUser> user = userService.findById(acc.getId().orElse(-1L));

        if (user.isPresent() && user.get().getStatus() == UserStatus.BLOCKED) {
            return createJsonWithMessage(bundle.getString(USER_BLOCKED_KEY));
        }

        return createUserSession(request, acc);
    }

    private CommandResponse createJsonWithMessage(String message) {
        return requestFactory.createJsonResponse(
                null, JsonResponseStatus.ERROR, message
        );
    }

    private CommandResponse createUserSession(CommandRequest request, Account acc) {
        String userPageCommand;

        request.clearSession();
        request.createSession();
        request.addToSession(USER_SESSION_ATTRIBUTE_NAME, acc);

        switch (acc.getRole()) {
        case ADMIN:
            userPageCommand = PagePaths.USER_CONTROL.getCommand();
            break;
        case DRIVER:
            userPageCommand = PagePaths.INCOMING_ORDER.getCommand();
            break;
        case CLIENT:
            userPageCommand = PagePaths.CLIENT_ORDER.getCommand();
            request.addToSession(LONGITUDE_SESSION_ATTRIBUTE_NAME, randomizer.getLongitude());
            request.addToSession(LATITUDE_SESSION_ATTRIBUTE_NAME, randomizer.getLatitude());
            break;
        default:
            userPageCommand = PagePaths.ERROR.getCommand();
        }

        return requestFactory.createRedirectJsonResponse(userPageCommand);
    }

    private Optional<Account> findAccount(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);

        return accountService.authenticate(login, password);
    }

    public static LoginCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final LoginCommand INSTANCE =
                new LoginCommand(
                        RequestFactoryImpl.getInstance(),
                        CoordinateRandomizerImpl.getInstance(),
                        EntityServiceFactory.getInstance().serviceFor(AccountService.class),
                        EntityServiceFactory.getInstance().serviceFor(UserService.class),
                        ResourceBundleExtractorImpl.getInstance()
                );
    }

}
