package com.sidorovich.pavel.buber.command;

public interface Command {

    CommandResponse execute(CommandRequest request);

    static Command of(String name) {
        return CommandRegistry.of(name);
    }

}
