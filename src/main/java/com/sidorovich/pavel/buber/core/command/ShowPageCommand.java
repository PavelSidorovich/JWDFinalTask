package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

public class ShowPageCommand extends CommonCommand {

    private final PagePaths pagePath;

    private ShowPageCommand(RequestFactory requestFactory,
                            PagePaths pagePath) {
        super(requestFactory);
        this.pagePath = pagePath;
    }

    public static ShowPageCommand getInstance(PagePaths pagePath) {
        return new ShowPageCommand(RequestFactoryImpl.getInstance(), pagePath);
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(pagePath.getPath());
    }

}
