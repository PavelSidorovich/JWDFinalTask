package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.exception.EmptySessionException;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
import com.sidorovich.pavel.buber.core.validator.PasswordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChangePasswordCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(ChangePasswordCommand.class);

    private static final String BASE_NAME = "l10n.msg.error";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String PASSWORD_REPEAT_REQUEST_PARAM_NAME = "passwordRepeat";

    private final BiValidator<String, String, Map<String, String>> passwordValidator;
    private final UserService userService;
    private final ResourceBundleExtractor resourceBundleExtractor;

    private ChangePasswordCommand(RequestFactory requestFactory,
                                  BiValidator<String, String, Map<String, String>> passwordValidator,
                                  UserService userService,
                                  ResourceBundleExtractor resourceBundleExtractor) {
        super(requestFactory);
        this.passwordValidator = passwordValidator;
        this.userService = userService;
        this.resourceBundleExtractor = resourceBundleExtractor;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            final Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME)
                                                     .orElseThrow(EmptySessionException::new);
            final Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            user.ifPresent(buberUser -> changePassword(request, buberUser));
        } catch (EmptySessionException e) {
            LOG.error(e);
        }

        return requestFactory.createRedirectResponse(PagePaths.ACCOUNT_CONTROL.getCommand());
    }

    private void changePassword(CommandRequest request, BuberUser user) {
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final String passwordRepeat = request.getParameter(PASSWORD_REPEAT_REQUEST_PARAM_NAME);
        ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(request, BASE_NAME);

        if (passwordValidator.validate(password, passwordRepeat, resourceBundle).isEmpty()) {
            final BuberUser editedUser = user.withAccount(
                    user.getAccount().withPasswordHash(password)
            );

            userService.updatePassword(editedUser);
        }
    }

    public static ChangePasswordCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand(
                RequestFactoryImpl.getInstance(),
                PasswordValidator.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
