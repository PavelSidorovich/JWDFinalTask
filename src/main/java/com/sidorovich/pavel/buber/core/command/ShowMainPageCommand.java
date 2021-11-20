package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.command.CommandRequest;
import com.sidorovich.pavel.buber.api.command.CommandResponse;

public class ShowMainPageCommand implements Command {

    private static final CommandResponse FORWARD_TO_MAIN_PAGE_RESPONSE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/main.jsp";
        }
    };

    private ShowMainPageCommand() {
    }

    private static class InstanceCreator {
        private static final ShowMainPageCommand INSTANCE = new ShowMainPageCommand();

    }

    public static ShowMainPageCommand getInstance() {
        return InstanceCreator.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return FORWARD_TO_MAIN_PAGE_RESPONSE;
    }
}
