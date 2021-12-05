package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.CoordinatesDao;
import com.sidorovich.pavel.buber.core.dao.UserOrderDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserOrderService implements EntityService<UserOrder> {

    private static final Logger LOG = LogManager.getLogger(UserOrderService.class);

    private final UserOrderDao orderDao;
    private final DriverService driverService;
    private final UserService userService;
    private final CoordinatesDao coordinatesDao;

    UserOrderService(UserOrderDao orderDao, DriverService driverService,
                     UserService userService,
                     CoordinatesDao coordinatesDao) {
        this.orderDao = orderDao;
        this.driverService = driverService;
        this.userService = userService;
        this.coordinatesDao = coordinatesDao;
    }

    public List<UserOrder> findByClient(BuberUser client) {
        return orderDao.findByClientId(client.getId().orElse(-1L))
                       .stream()
                       .map(this::buildOrder)
                       .collect(Collectors.toList());
    }

    public List<UserOrder> findByDriver(Driver driver) {
        return orderDao.findByDriverId(driver.getId().orElse(-1L))
                       .stream()
                       .map(this::buildOrder)
                       .collect(Collectors.toList());
    }

    @Override
    public UserOrder save(UserOrder order) {
        try {
            UserOrder saved = orderDao.save(order);

            return UserOrder.with()
                            .id(saved.getId().orElse(-1L))
                            .client(order.getClient())
                            .driver(order.getDriver())
                            .price(order.getPrice())
                            .initialCoordinates(order.getInitialCoordinates())
                            .endCoordinates(order.getEndCoordinates())
                            .status(order.getStatus())
                            .build();
        } catch (SQLException e) {
            LOG.error(e);
            return order;
        }
    }

    @Override
    public Optional<UserOrder> findById(Long id) {
        return orderDao.findById(id)
                       .map(this::buildOrder);
    }

    @Override
    public List<UserOrder> findAll() {
        return orderDao.findAll().stream()
                       .map(this::buildOrder)
                       .collect(Collectors.toList());
    }

    private UserOrder buildOrder(UserOrder order) {
        return UserOrder.with()
                        .id(order.getId().orElse(-1L))
                        .client(userService.findById(order.getClient().getId().orElse(-1L))
                                           .orElse(null))
                        .driver(driverService.findById(order.getDriver().getId().orElse(-1L))
                                             .orElse(Driver.empty()))
                        .price(order.getPrice())
                        .initialCoordinates(
                                coordinatesDao.findById(order.getInitialCoordinates().getId()
                                                             .orElse(-1L))
                                              .orElse(null))
                        .endCoordinates(coordinatesDao.findById(order.getEndCoordinates().getId()
                                                                     .orElse(-1L))
                                                      .orElse(null))
                        .status(order.getStatus())
                        .build();
    }

    @Override
    public UserOrder update(UserOrder order) {
        try {
            UserOrder updatedOrder = orderDao.update(order);

            return UserOrder.with()
                            .id(updatedOrder.getId().orElse(-1L))
                            .client(order.getClient())
                            .driver(driverService.findById(updatedOrder.getDriver().getId()
                                                                       .orElse(-1L))
                                                 .orElse(new Driver(
                                                         BuberUser.with().build(), null,
                                                         null, null)))
                            .price(updatedOrder.getPrice())
                            .initialCoordinates(order.getInitialCoordinates())
                            .endCoordinates(order.getEndCoordinates())
                            .status(updatedOrder.getStatus())
                            .build();
        } catch (SQLException e) {
            LOG.error(e);
        }

        return order;
    }

    @Override
    public boolean delete(Long id) {
        return orderDao.delete(id);
    }

}
