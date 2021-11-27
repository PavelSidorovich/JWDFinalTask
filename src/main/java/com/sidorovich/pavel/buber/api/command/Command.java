package com.sidorovich.pavel.buber.api.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;

public interface Command {

    CommandResponse execute(CommandRequest request);

}
