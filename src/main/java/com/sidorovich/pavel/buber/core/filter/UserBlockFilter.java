package com.sidorovich.pavel.buber.core.filter;

import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;
import com.sidorovich.pavel.buber.core.controller.PlainCommandRequest;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserService;
import com.sidorovich.pavel.buber.core.util.ResourceBundleExtractorImpl;
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
import java.util.Optional;
import java.util.ResourceBundle;

@WebFilter(filterName = "UserBlockFilter")
public class UserBlockFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(UserBlockFilter.class);

    private static final String BASE_NAME = "l10n.msg.error";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String SHOW_ERROR_COMMAND = "command=show_error";
    private static final String BLOCKED_KEY = "msg.blocked";
    private static final String ERROR_PAGE_MESSAGE_ATTR_PARAM_NAME = "errorPageMessage";

    private final UserService userService;
    private final ResourceBundleExtractor resourceBundleExtractor;

    public UserBlockFilter() {
        resourceBundleExtractor = ResourceBundleExtractorImpl.getInstance();
        this.userService = EntityServiceFactory.getInstance().serviceFor(UserService.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final String command = req.getQueryString();

        if (SHOW_ERROR_COMMAND.equals(command) || currentUserIsNotBlocked(req)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            ResourceBundle resourceBundle = resourceBundleExtractor.extractResourceBundle(
                    new PlainCommandRequest(req), BASE_NAME
            );

            request.setAttribute(ERROR_PAGE_MESSAGE_ATTR_PARAM_NAME, resourceBundle.getString(BLOCKED_KEY));
            servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean currentUserIsNotBlocked(HttpServletRequest request) {
        UserStatus currentUserStatus = retrieveCurrentUserStatus(request);

        return UserStatus.BLOCKED != currentUserStatus;
    }

    private UserStatus retrieveCurrentUserStatus(HttpServletRequest request) {
        Optional<Account> account = Optional.ofNullable(request.getSession(false))
                                            .map(s -> (Account) s.getAttribute(USER_SESSION_ATTRIBUTE_NAME));

        if (account.isPresent()) {
            Account acc = account.get();
            Optional<BuberUser> userFromDb = userService.findByPhone(acc.getPhone());

            LOG.trace("Checking user status for account: {}", acc);
            if (userFromDb.isPresent()) {
                return userFromDb.get().getStatus();
            }
        }

        return UserStatus.ACTIVE;
    }

}
