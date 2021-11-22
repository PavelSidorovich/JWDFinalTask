package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.DriverDao;
import com.sidorovich.pavel.buber.core.dao.TaxiDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverService implements EntityService<Driver> {

    private final DriverDao driverDao;
    private final UserService userService;
    private final TaxiDao taxiDao;

    DriverService(DriverDao driverDao, UserService userService,
                         TaxiDao taxiDao) {
        this.driverDao = driverDao;
        this.userService = userService;
        this.taxiDao = taxiDao;
    }

    @Override
    public Driver save(Driver driver) throws SQLException {
        BuberUser user = userService.save(driver.getUser());

        try {
            return driverDao.save(driver.withBuberUser(user))
                            .withBuberUser(user);
        } catch (SQLException e) {
            userService.delete(user.getId().orElse(-1L));
            throw e;
        }
    }

    @Override
    public Optional<Driver> findById(Long id) {
        return driverDao.findById(id)
                        .map(this::buildDriver);
    }

    @Override
    public List<Driver> findAll() {
        return driverDao.findAll().stream()
                        .map(this::buildDriver)
                        .collect(Collectors.toList());
    }

    private Driver buildDriver(Driver driver) {
        return driver
                .withBuberUser(userService.findById(driver.getId().orElse(-1L))
                                          .orElse(driver.getUser()))
                .withTaxi(taxiDao.findById(driver.getTaxi().getId().orElse(-1L))
                                 .orElse(null));
    }

    @Override
    public Driver update(Driver driver) {
        BuberUser user = userService.update(driver.getUser());
        Driver update = driverDao.update(driver);

        return update.withBuberUser(user);
    }

    @Override
    public boolean delete(Long id) {
        return userService.delete(id);
    }

}