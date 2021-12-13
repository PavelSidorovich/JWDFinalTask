package com.sidorovich.pavel.buber.core.filter;

import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.core.service.BonusService;
import com.sidorovich.pavel.buber.core.service.EntityServiceFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebFilter(filterName = "ExpiredBonusesFilter")
public class ExpiredBonusesFilter implements Filter {

    private static final long INVALID_INDEX = -1L;

    private final BonusService bonusService;

    public ExpiredBonusesFilter() {
        this.bonusService = EntityServiceFactory.getInstance().serviceFor(BonusService.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final Date dateNow = Date.valueOf(LocalDate.now());
        List<Bonus> bonusesToDelete = bonusService.findAll().parallelStream()
                                                  .filter(bonus -> bonus.getExpireDate().before(dateNow))
                                                  .collect(Collectors.toList());

        bonusesToDelete.forEach(bonus -> bonusService.delete(bonus.getId().orElse(INVALID_INDEX)));
        chain.doFilter(request, response);
    }

}
