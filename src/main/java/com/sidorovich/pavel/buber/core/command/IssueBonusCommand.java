package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.validator.Validator;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.validator.BonusValidator;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IssueBonusCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(IssueBonusCommand.class);

    private static final String ID_ARRAY_REQUEST_PARAM_NAME = "idArray";
    private static final String SPLIT_REGEX = ",";
    private static final String DISCOUNT_REQUEST_PARAM_NAME = "discount";
    private static final String INVALID_DISCOUNT_VALUE = "Valid discount value is required";
    private static final String NO_USERS_ARE_SELECTED = "No users are selected";
    private static final String USERS_REQUEST_PARAM_NAME = "users";
    private static final String EXPIRE_DATE_REQUEST_PARAM_NAME = "expireDate";
    private static final String SUCCESSFUL_ISSUING_BONUSES = "Successful issuing bonuses to users";
    private static final String EXPIRE_DATE_PARAM_NAME = "expireDate";
    private static final String INVALID_EXPIRE_DATE = "Valid date is required";

    private final UserService userService;
    private final BonusService bonusService;
    private final Validator<Bonus, Map<String, String>> validator;

    private IssueBonusCommand(RequestFactory requestFactory,
                              UserService userService,
                              BonusService bonusService,
                              Validator<Bonus, Map<String, String>> validator) {
        super(requestFactory);
        this.userService = userService;
        this.bonusService = bonusService;
        this.validator = validator;
    }

    private static class Holder {
        private static final IssueBonusCommand INSTANCE = new IssueBonusCommand(
                RequestFactoryImpl.getInstance(),
                EntityServiceFactory.getInstance().serviceFor(UserService.class),
                EntityServiceFactory.getInstance().serviceFor(BonusService.class),
                BonusValidator.getInstance());
    }

    public static IssueBonusCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Map<String, String> errorsByMessages = new HashMap<>();
        String idArray = request.getParameter(ID_ARRAY_REQUEST_PARAM_NAME);
        Double discount = getDiscount(request, errorsByMessages);
        Date expireDate = getExpireDate(request, errorsByMessages);

        if (idArray.isEmpty()) {
            errorsByMessages.put(USERS_REQUEST_PARAM_NAME, NO_USERS_ARE_SELECTED);
        } else if (discount != null && expireDate != null) {
            List<BuberUser> users = getUsers(request);

            errorsByMessages.putAll(validator.validate(new Bonus(discount, expireDate, null)));
            if (errorsByMessages.isEmpty()) {
                saveBonuses(expireDate, discount, users);
                return requestFactory.createJsonResponse(
                        null, JsonResponseStatus.SUCCESS, SUCCESSFUL_ISSUING_BONUSES);
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

    private Date getExpireDate(CommandRequest request, Map<String, String> errorsByMessages) {
        Date expireDate = null;

        try {
            expireDate = Date.valueOf(request.getParameter(EXPIRE_DATE_REQUEST_PARAM_NAME));
        } catch (Exception e) {
            errorsByMessages.put(EXPIRE_DATE_PARAM_NAME, INVALID_EXPIRE_DATE);
        }

        return expireDate;
    }

    private Double getDiscount(CommandRequest request, Map<String, String> errorsByMessages) {
        Double discount = null;

        try {
            discount = Double.parseDouble(request.getParameter(DISCOUNT_REQUEST_PARAM_NAME));
        } catch (NumberFormatException e) {
            errorsByMessages.put(DISCOUNT_REQUEST_PARAM_NAME, INVALID_DISCOUNT_VALUE);
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

}
