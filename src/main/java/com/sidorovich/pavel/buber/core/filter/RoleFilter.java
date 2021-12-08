package com.sidorovich.pavel.buber.core.filter;

import com.sidorovich.pavel.buber.api.command.Command;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Role;
import com.sidorovich.pavel.buber.core.command.CommandRegistry;
import com.sidorovich.pavel.buber.core.service.AccountService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebFilter(filterName = "RoleFilter")
public class RoleFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(RoleFilter.class);

    private static final String COMMAND_PARAM_NAME = "command";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String ERROR_PAGE_MESSAGE_ATTR_PARAM_NAME = "errorPageMessage";
    private static final String PERMISSION_DENIED_MSG = "You have not permission to proceed this action";

    private final AccountService accountService;
    private final Map<Role, Set<Command>> commandsByRoles;

    public RoleFilter() {
        accountService = EntityServiceFactory.getInstance().serviceFor(AccountService.class);
        commandsByRoles = new EnumMap<>(Role.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        for (CommandRegistry command : CommandRegistry.values()) {
            for (Role allowedRole : command.getAllowedRoles()) {
                final Set<Command> commands = commandsByRoles.computeIfAbsent(allowedRole, k -> new HashSet<>());
                commands.add(command.getCommand());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final String commandName = req.getParameter(COMMAND_PARAM_NAME);

        LOG.trace("Checking permissions for command. commandName: {}", commandName);
        if (currentUserHasPermissionForCommand(commandName, req)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            req.setAttribute(ERROR_PAGE_MESSAGE_ATTR_PARAM_NAME, PERMISSION_DENIED_MSG);
            servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean currentUserHasPermissionForCommand(String commandName, HttpServletRequest request) {
        Role currentUserRole = retrieveCurrentUserRole(request);
        final Command command = CommandRegistry.of(commandName);
        final Set<Command> allowedCommands = commandsByRoles.get(currentUserRole);

        return allowedCommands.contains(command);
    }

    private Role retrieveCurrentUserRole(HttpServletRequest request) {
        Optional<Account> account = Optional.ofNullable(request.getSession(false))
                                            .map(s -> (Account) s.getAttribute(USER_SESSION_ATTRIBUTE_NAME));
        if (account.isPresent()) {
            Account acc = account.get();
            Optional<Account> accFromDb = accountService.readAccountByPhone(acc.getPhone());

            if (accFromDb.isPresent()
                && accFromDb.get().getRole() == acc.getRole()
                && accFromDb.get().getId().orElse(-1L).equals(acc.getId().orElse(-1L))) {
                return accFromDb.get().getRole();
            }
        }

        return Role.UNAUTHORIZED;
    }

}
