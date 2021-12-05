package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.controller.PagePaths;

import java.util.Arrays;
import java.util.List;

import static com.sidorovich.pavel.buber.api.model.Role.*;

public enum CommandRegistry {

    MAIN_PAGE(ShowPageCommand.getInstance(PagePaths.MAIN), "main"),
    SHOW_LOGIN_PAGE(ShowPageCommand.getInstance(PagePaths.LOGIN), "show_login"),
    LOGIN(LoginCommand.getInstance(), "login"),
    LOGOUT(LogoutCommand.getInstance(), "logout"),
    USER_REGISTER_PAGE(ShowPageCommand.getInstance(PagePaths.REGISTER), "show_user_register"),
    USER_REGISTER(UserRegisterCommand.getInstance(), "user_register"),
    USER_CONTROL_PAGE(ShowPageCommand.getInstance(PagePaths.USER_CONTROL), "show_user_control", ADMIN),
    GET_USERS(GetUsersCommand.getInstance(), "get_users", ADMIN),
    BLOCK_USER(BlockUserCommand.getInstance(), "block_user", ADMIN),
    DRIVER_REGISTER_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_REGISTER), "show_driver_register"),
    DRIVER_APPLICATION(DriverApplicationCommand.getInstance(), "driver_application"),
    DRIVER_APPLICATION_SUCCESS_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_APPLICATION_SUCCESS),
                                    "successful_application"),
    DRIVER_APPLICATIONS_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_APPLICATIONS), "driver_applications", ADMIN),
    GET_DRIVER_APPLICATIONS(GetDriversCommand.getInstance(), "get_driver_applications", ADMIN),
    GET_DRIVER(GetDriverCommand.getInstance(), "get_driver", ADMIN),
    UPDATE_DRIVER_STATUS(DriverStatusUpdateCommand.getInstance(), "update_driver_status", ADMIN, DRIVER),
    GET_USERS_BY_ORDER_AMOUNT(GetUsersByAmountOfOrdersCommand.getInstance(), "get_users_by_order_amount", ADMIN),
    GET_BONUSES(GetBonusesCommand.getInstance(), "get_bonuses", ADMIN),
    DELETE_BONUS(DeleteBonusCommand.getInstance(), "delete_bonus", ADMIN, CLIENT),
    BONUS_ISSUE_PAGE(ShowPageCommand.getInstance(PagePaths.ISSUE_BONUSES), "show_bonuses", ADMIN),
    ISSUE_BONUS(IssueBonusCommand.getInstance(), "issue_bonus", ADMIN),
    MY_BONUSES(ShowPageCommand.getInstance(PagePaths.MY_BONUSES), "my_bonuses", CLIENT),
    CALL_TAXI(CallTaxiCommand.getInstance(), "call_taxi", CLIENT),
    GET_TAXI_PHOTO(GetTaxiPhotoPathCommand.getInstance(), "get_taxi_photo"),
//    GET_MY_BONUSES(GetMyBonusesCommand.getInstance(), "get_my_bonuses", CLIENT),
    GET_ORDER_PRICE(GetOrderPriceCommand.getInstance(), "get_order_price", CLIENT),
    MAKE_ORDER_PAGE(MakeOrderCommand.getInstance(), "make_order", CLIENT),
    INCOMING_ORDER_PAGE(IncomingOrderCommand.getInstance(), "incoming_order", DRIVER),
    CANCEL_USER_ORDER(CancelOrderCommand.getInstance(), "cancel_order", CLIENT),
    DRIVER_PROCESS_ORDER(DriverProcessOrderCommand.getInstance(), "driver_process_order", DRIVER),
    CONFIRM_PAYMENT(ConfirmPaymentCommand.getInstance(), "confirm_payment", DRIVER),
    GET_DRIVER_STATUS(GetDriverStatusCommand.getInstance(), "get_driver_status", DRIVER),
    ERROR_PAGE(ShowPageCommand.getInstance(PagePaths.ERROR), "show_error"),
    DEFAULT(ShowPageCommand.getInstance(PagePaths.MAIN), ""),
    ;

    private final Command command;
    private final String path;
    private final List<Role> allowedRoles;

    CommandRegistry(Command command, String path, Role... roles) {
        this.command = command;
        this.path = path;
        this.allowedRoles = roles != null && roles.length > 0
                ? Arrays.asList(roles)
                : valuesAsList();
    }

    public Command getCommand() {
        return command;
    }

    public String getPath() {
        return path;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    public static Command of(String name) {
        for (CommandRegistry constant : values()) {
            if (constant.path.equalsIgnoreCase(name)) {
                return constant.command;
            }
        }
        return DEFAULT.command;
    }

}
