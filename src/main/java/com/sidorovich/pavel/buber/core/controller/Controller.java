package com.sidorovich.pavel.buber.core.controller;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.core.command.CommandRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    private static final String COMMAND_NAME_PARAM = "command";

    private final RequestFactory requestFactory = RequestFactoryImpl.getInstance();

    @Override
    public void init() {
        ConnectionPool.locking().init();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.trace("caught req and resp in doGet method");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.trace("caught req and resp in doPost method");
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        final String commandName = request.getParameter(COMMAND_NAME_PARAM);
        final Command command = CommandRegistry.of(commandName);
        final CommandRequest commandRequest = requestFactory.createRequest(request);
        final CommandResponse commandResponse = command.execute(commandRequest);

        proceedWithResponse(request, response, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            forwardOrRedirectToResponseLocation(req, resp, commandResponse);
        } catch (ServletException e) {
            LOG.error("servlet exception occurred", e);
        } catch (IOException e) {
            LOG.error("IO exception occurred", e);
        }
    }

    private void forwardOrRedirectToResponseLocation(HttpServletRequest req, HttpServletResponse resp,
                                                     CommandResponse commandResponse)
            throws IOException, ServletException {
        if (commandResponse.isRedirect()) {
            resp.sendRedirect(commandResponse.getPath());
        } else {
            final String desiredPath = commandResponse.getPath();
            final RequestDispatcher dispatcher = req.getRequestDispatcher(desiredPath);

            dispatcher.forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.locking().shutDown();
    }
}