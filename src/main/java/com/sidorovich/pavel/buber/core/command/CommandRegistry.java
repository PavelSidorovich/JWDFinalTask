package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.model.Role;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.getInstance(), "main"),
    SHOW_LOGIN_PAGE(ShowLoginPageCommand.getInstance(), "show_login"),
    SHOW_ADMIN_PAGE(ShowAdminPageCommand.getInstance(), "show_admin"),
    LOGIN(LoginCommand.getInstance(), "login"),
    LOGOUT(LogoutCommand.getInstance(), "logout"),
    DEFAULT(ShowMainPageCommand.getInstance(), ""),
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
