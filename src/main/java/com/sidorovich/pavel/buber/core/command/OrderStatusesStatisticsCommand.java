package com.sidorovich.pavel.buber.core.command;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.controller.CommandResponse;
import com.sidorovich.pavel.buber.api.controller.RequestFactory;
import com.sidorovich.pavel.buber.core.controller.JsonResponseStatus;
import com.sidorovich.pavel.buber.core.controller.RequestFactoryImpl;
import com.sidorovich.pavel.buber.core.service.StatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;

public class OrderStatusesStatisticsCommand extends CommonCommand {

    private static final Logger LOG = LogManager.getLogger(OrderStatusesStatisticsCommand.class);

    private final StatisticsService statisticsService;

    public OrderStatusesStatisticsCommand(RequestFactory requestFactory,
                                          StatisticsService statisticsService) {
        super(requestFactory);
        this.statisticsService = statisticsService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            final Map<String, Integer> chartData = statisticsService.retrievePieChartData();

            return requestFactory.createJsonResponse(chartData, JsonResponseStatus.SUCCESS);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return requestFactory.createJsonResponse(null, JsonResponseStatus.ERROR);
    }

    public static OrderStatusesStatisticsCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OrderStatusesStatisticsCommand INSTANCE = new OrderStatusesStatisticsCommand(
                RequestFactoryImpl.getInstance(),
                StatisticsService.getInstance());
    }

}
