package com.sidorovich.pavel.buber.api.command;

public interface Command {

    CommandResponse execute(CommandRequest request);
    
}
