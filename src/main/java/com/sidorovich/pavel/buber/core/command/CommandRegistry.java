package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.controller.PagePaths;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {

    MAIN_PAGE(ShowPageCommand.getInstance(PagePaths.MAIN, false), "main"),
    SHOW_LOGIN_PAGE(ShowPageCommand.getInstance(PagePaths.LOGIN, false), "show_login"),
    LOGIN(LoginCommand.getInstance(), "login"),
    LOGOUT(LogoutCommand.getInstance(), "logout"),
    USER_REGISTER_PAGE(ShowPageCommand.getInstance(PagePaths.REGISTER, false), "show_user_register"),
    USER_REGISTER(UserRegisterCommand.getInstance(), "user_register"),
    SHOW_ADMIN_PAGE(ShowPageCommand.getInstance(PagePaths.ADMIN_PAGE, false), "show_admin", Role.ADMIN),
    ERROR(ShowPageCommand.getInstance(PagePaths.ERROR, false), "show_error"),
    DEFAULT(ShowPageCommand.getInstance(PagePaths.MAIN, false), ""),
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
