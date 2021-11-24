package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.PagePaths;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;

public class ShowPageCommand extends CommonCommand {

    private final PagePaths pagePath;
    private final boolean isRedirect;

    private ShowPageCommand(RequestFactory requestFactory,
                            PagePaths pagePath, boolean isRedirect) {
        super(requestFactory);
        this.pagePath = pagePath;
        this.isRedirect = isRedirect;
    }

    public static ShowPageCommand getInstance(PagePaths pagePath, boolean isRedirect) {
        return new ShowPageCommand(RequestFactoryImpl.getInstance(), pagePath, isRedirect);
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return isRedirect
                ? requestFactory.createRedirectResponse(pagePath.getCommand())
                : requestFactory.createForwardResponse(pagePath.getPath());
    }

}
