package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;

// TODO: 10/20/2021 learn how to inject pool 
public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.getInstance(), "main_page"),
//    SHOW_ACCOUNTS(new ShowAccountPageCommand(new AccountDao(ConnectionPool.locking())), "show_accounts"),
    DEFAULT(ShowMainPageCommand.getInstance(), ""),
    ;

    private final Command command;
    private final String path;

    CommandRegistry(Command command, String path) {
        this.command = command;
        this.path = path;
    }

    public Command getCommand() {
        return command;
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
