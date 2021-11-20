package com.sidorovich.pavel.buber.api.command;

import com.sidorovich.pavel.buber.core.command.CommandRegistry;

public interface Command {

    CommandResponse execute(CommandRequest request);

    static Command of(String name) {
        return CommandRegistry.of(name);
    }

}
