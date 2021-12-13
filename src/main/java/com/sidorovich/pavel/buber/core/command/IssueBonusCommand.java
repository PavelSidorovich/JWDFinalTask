package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
import com.sidorovich.pavel.buber.core.validator.BonusValidator;
import com.sidorovich.pavel.buber.api.exception.DuplicateKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class IssueBonusCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(IssueBonusCommand.class);

    private static final String ERRORS_BASE_NAME = "l10n.msg.error";
    private static final String SUCCESS_BASE_NAME = "l10n.msg.success";
    private static final String ID_ARRAY_REQUEST_PARAM_NAME = "idArray";
    private static final String SPLIT_REGEX = ",";
    private static final String DISCOUNT_REQUEST_PARAM_NAME = "discount";
    private static final String USERS_REQUEST_PARAM_NAME = "users";
    private static final String EXPIRE_DATE_REQUEST_PARAM_NAME = "expireDate";
    private static final String EXPIRE_DATE_PARAM_NAME = "expireDate";
    private static final String INVALID_NO_SELECTED_USERS_KEY = "msg.invalid.noSelectedUsers";
    private static final String BONUSES_ISSUED_KEY = "msg.bonusesIssued";
    private static final String INVALID_DISCOUNT_KEY = "msg.invalid.discount";
    private static final String INVALID_DATA_KEY = "msg.invalid.data";

    private final UserService userService;
    private final BonusService bonusService;
    private final Validator<Bonus, Map<String, String>> validator;
    private final ResourceBundleExtractor resourceBundleExtractor;

    private IssueBonusCommand(RequestFactory requestFactory,
                              UserService userService,
                              BonusService bonusService,
                              Validator<Bonus, Map<String, String>> validator,
                              ResourceBundleExtractor resourceBundleExtractor) {
        super(requestFactory);
        this.userService = userService;
        this.bonusService = bonusService;
        this.validator = validator;
        this.resourceBundleExtractor = resourceBundleExtractor;
    }


    @Override
    public CommandResponse execute(CommandRequest request) {
        ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(request, ERRORS_BASE_NAME);
        ResourceBundle successResourceBundle = resourceBundleExtractor.extractResourceBundle(request, SUCCESS_BASE_NAME);
        Map<String, String> errorsByMessages = new HashMap<>();
        Double discount = getDiscount(request, errorsByMessages, resourceBundle);
        Date expireDate = getExpireDate(request, errorsByMessages, resourceBundle);
        String idArray = request.getParameter(ID_ARRAY_REQUEST_PARAM_NAME);

        if (idArray.isEmpty()) {
            errorsByMessages.put(USERS_REQUEST_PARAM_NAME, resourceBundle.getString(INVALID_NO_SELECTED_USERS_KEY));
        } else if (discount != null && expireDate != null) {
            List<BuberUser> users = getUsers(request);

            errorsByMessages.putAll(validator.validate(new Bonus(discount, expireDate, null), resourceBundle));
            if (errorsByMessages.isEmpty()) {
                saveBonuses(expireDate, discount, users);
                return requestFactory.createJsonResponse(
                        null, JsonResponseStatus.SUCCESS, successResourceBundle.getString(BONUSES_ISSUED_KEY));
            }
        }

        return requestFactory.createJsonResponse(errorsByMessages, JsonResponseStatus.ERROR);
    }

    private void saveBonuses(Date expireDate, Double discount, List<BuberUser> users) {
        final Double finalDiscount = discount;
        users.forEach(user -> {
                          try {
                              bonusService.save(new Bonus(finalDiscount, expireDate, user));
                          } catch (DuplicateKeyException e) {
                              LOG.error(e);
                          }
                      }
        );
    }

    private Date getExpireDate(CommandRequest request, Map<String, String> errorsByMessages,
                               ResourceBundle resourceBundle) {
        Date expireDate = null;

        try {
            expireDate = Date.valueOf(request.getParameter(EXPIRE_DATE_REQUEST_PARAM_NAME));
        } catch (Exception e) {
            errorsByMessages.put(EXPIRE_DATE_PARAM_NAME, resourceBundle.getString(INVALID_DATA_KEY));
        }

        return expireDate;
    }

    private Double getDiscount(CommandRequest request, Map<String, String> errorsByMessages,
                               ResourceBundle resourceBundle) {
        Double discount = null;

        try {
            discount = Double.parseDouble(request.getParameter(DISCOUNT_REQUEST_PARAM_NAME));
        } catch (NumberFormatException e) {
            errorsByMessages.put(DISCOUNT_REQUEST_PARAM_NAME,resourceBundle.getString(INVALID_DISCOUNT_KEY));
        }

        return discount;
    }

    private List<BuberUser> getUsers(CommandRequest request) {
        List<Long> ids = Arrays.stream(request.getParameter(ID_ARRAY_REQUEST_PARAM_NAME)
                                              .split(SPLIT_REGEX))
                               .map(Long::parseLong)
                               .collect(Collectors.toList());

        return ids.stream()
                  .map(id -> userService.findById(id).orElse(null))
                  .collect(Collectors.toList());
    }

    public static IssueBonusCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final IssueBonusCommand INSTANCE = new IssueBonusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class),
                BonusValidator.getInstance(),
                ResourceBundleExtractorImpl.getInstance()
        );
    }

}
