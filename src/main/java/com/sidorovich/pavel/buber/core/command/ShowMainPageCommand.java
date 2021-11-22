package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

public class ShowMainPageCommand implements Command {

    private final RequestFactory requestFactory;

    private ShowMainPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    private static class Holder {
        private static final ShowMainPageCommand INSTANCE = new ShowMainPageCommand(RequestFactoryImpl.getInstance());
    }

    public static ShowMainPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePaths.MAIN.getPath());
    }

}
