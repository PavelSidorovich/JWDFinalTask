package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.api.validator.BiValidator;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
import com.sidorovich.pavel.buber.core.validator.EmailValidator;
import com.sidorovich.pavel.buber.core.validator.PersonalInfoValidator;
import com.sidorovich.pavel.buber.api.exception.EmptySessionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditPersonalInfoCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(EditPersonalInfoCommand.class);

    private static final String BASE_NAME = "l10n.msg.error";
    private static final String USER_SESSION_PARAM_NAME = "user";
    private static final String LAST_NAME_REQUEST_PARAM_NAME = "lastName";
    private static final String FIRST_NAME_REQUEST_PARAM_NAME = "firstName";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final boolean EMAIL_IS_OPTIONAL = true;

    private final BiValidator<String, Boolean, Map<String, String>> emailValidator;
    private final Validator<BuberUser, Map<String, String>> personalInfoValidator;
    private final UserService userService;
    private final ResourceBundleExtractor resourceBundleExtractor;

    private EditPersonalInfoCommand(RequestFactory requestFactory,
                                    BiValidator<String, Boolean, Map<String, String>> emailValidator,
                                    Validator<BuberUser, Map<String, String>> personalInfoValidator,
                                    UserService userService,
                                    ResourceBundleExtractor resourceBundleExtractor) {
        super(requestFactory);
        this.emailValidator = emailValidator;
        this.personalInfoValidator = personalInfoValidator;
        this.userService = userService;
        this.resourceBundleExtractor = resourceBundleExtractor;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            Account account = (Account) request.retrieveFromSession(USER_SESSION_PARAM_NAME)
                                               .orElseThrow(EmptySessionException::new);
            Optional<BuberUser> user = userService.findByPhone(account.getPhone());

            user.ifPresent(buberUser -> updateUserPersonalInfo(request, buberUser));
        } catch (EmptySessionException e) {
            LOG.error(e);
        }

        return requestFactory.createRedirectResponse(PagePaths.ACCOUNT_CONTROL.getCommand());
    }

    private void updateUserPersonalInfo(CommandRequest request, BuberUser user) {
        String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
        BuberUser editedUser = BuberUser.with()
                                        .lastName(request.getParameter(LAST_NAME_REQUEST_PARAM_NAME))
                                        .firstName(request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME))
                                        .email(email)
                                        .cash(user.getCash())
                                        .status(user.getStatus())
                                        .account(user.getAccount())
                                        .build();
        ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(request, BASE_NAME);

        updateUserIfInfoIsValid(email, editedUser, resourceBundle);
    }

    private void updateUserIfInfoIsValid(String email, BuberUser editedUser, ResourceBundle resourceBundle) {
        Map<String, String> errorsByMessages = new HashMap<>(
                personalInfoValidator.validate(editedUser, resourceBundle)
        );

        errorsByMessages.putAll(emailValidator.validate(email, EMAIL_IS_OPTIONAL, resourceBundle));
        if (errorsByMessages.isEmpty()) {
            userService.update(editedUser);
        }
    }

    public static EditPersonalInfoCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EditPersonalInfoCommand INSTANCE = new EditPersonalInfoCommand(
                RequestFactoryImpl.getInstance(),
                EmailValidator.getInstance(),
                PersonalInfoValidator.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
