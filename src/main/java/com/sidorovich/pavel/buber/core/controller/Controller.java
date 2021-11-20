package com.sidorovich.pavel.buber.core.controller;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.command.CommandResponse;
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
public class MainServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(MainServlet.class);

    @Override
    public void init() {
        ConnectionPool.locking().init();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.trace("caught request and response");
        final String commandName = request.getParameter("command");
        final Command command = CommandRegistry.of(commandName);
        final CommandResponse commandResponse = command.execute(request::setAttribute);

        proceedWithResponse(request, response, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            forwardOrRedirectToResponseLocation(req, resp, commandResponse);
        } catch (ServletException e) {
            LOG.error("servlet exception occurred", e);
        } catch (IOException e) {
            LOG.error(e);
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