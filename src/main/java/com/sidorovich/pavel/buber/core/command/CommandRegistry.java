package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.controller.PagePaths;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {

    MAIN_PAGE(ShowPageCommand.getInstance(PagePaths.MAIN), "main"),
    SHOW_LOGIN_PAGE(ShowPageCommand.getInstance(PagePaths.LOGIN), "show_login"),
    LOGIN(LoginCommand.getInstance(), "login"),
    LOGOUT(LogoutCommand.getInstance(), "logout"),
    USER_REGISTER_PAGE(ShowPageCommand.getInstance(PagePaths.REGISTER), "show_user_register"),
    USER_REGISTER(UserRegisterCommand.getInstance(), "user_register"),
    USER_CONTROL_PAGE(ShowPageCommand.getInstance(PagePaths.USER_CONTROL_PAGE), "show_user_control", Role.ADMIN),
    GET_USERS(GetUsersCommand.getInstance(), "get_users", Role.ADMIN),
    BLOCK_USER(BlockUserCommand.getInstance(), "block_user", Role.ADMIN),
    DRIVER_REGISTER_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_REGISTER), "show_driver_register"),
    DRIVER_APPLICATION(DriverApplicationCommand.getInstance(), "driver_application"),
    DRIVER_APPLICATION_SUCCESS_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_APPLICATION_SUCCESS),
                                    "successful_application"),
    DRIVER_APPLICATIONS_PAGE(ShowPageCommand.getInstance(PagePaths.DRIVER_APPLICATIONS), "driver_applications", Role.ADMIN),
    GET_DRIVER_APPLICATIONS(GetDriverApplications.getInstance(), "get_driver_applications", Role.ADMIN),
    ERROR(ShowPageCommand.getInstance(PagePaths.ERROR), "show_error"),
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
                : Role.valuesAsList();
    }

    public Command getCommand() {
        return command;
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
