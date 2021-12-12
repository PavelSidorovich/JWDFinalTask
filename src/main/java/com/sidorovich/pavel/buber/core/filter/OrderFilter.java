package com.sidorovich.pavel.buber.core.filter;

import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.service.DriverService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;
import com.sidorovich.pavel.buber.core.service.UserOrderService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.sidorovich.pavel.buber.api.model.OrderStatus.*;
import static com.sidorovich.pavel.buber.api.model.UserStatus.*;

@WebFilter(filterName = "OrderFilter")
public class OrderFilter implements Filter {

    private final UserOrderService userOrderService;
    private final DriverService driverService;

    public OrderFilter() {
        this.userOrderService = EntityServiceFactory.getInstance().serviceFor(UserOrderService.class);
        this.driverService = EntityServiceFactory.getInstance().serviceFor(DriverService.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        List<UserOrder> toCancel =
                userOrderService.findAll().stream()
                                .filter(order -> (order.getStatus() == NEW
                                                  || order.getStatus() == IN_PROCESS)
                                                 && (order.getDriver().getUser().getStatus() == BLOCKED
                                                     || order.getClient().getStatus() == BLOCKED)
                                ).collect(Collectors.toList());

        for (UserOrder userOrder : toCancel) {
            userOrderService.update(userOrder.withStatus(CANCELLED));
            driverService.update(userOrder.getDriver().withDriverStatus(DriverStatus.REST));
        }

        chain.doFilter(request, response);
    }

}
