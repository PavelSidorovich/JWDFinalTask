package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.DriverDao;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.exception.EntitySavingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverService implements EntityService<Driver> {

    private static final Logger LOG = LogManager.getLogger(DriverService.class);

    private final DriverDao driverDao;
    private final UserService userService;
    private final TaxiService taxiService;

    DriverService(DriverDao driverDao, UserService userService,
                  TaxiService taxiService) {
        this.driverDao = driverDao;
        this.userService = userService;
        this.taxiService = taxiService;
    }

    @Override
    public Driver save(Driver driver) throws DuplicateKeyException {
        BuberUser user = userService.save(driver.getUser());
        Taxi taxi = taxiService.save(driver.getTaxi());

        try {
            return driverDao.save(driver.withBuberUser(user)
                                        .withTaxi(taxi))
                            .withBuberUser(user)
                            .withTaxi(taxi);
        } catch (SQLException e) {
            userService.delete(user.getId().orElse(-1L));
            taxiService.delete(taxi.getId().orElse(-1L));

            throw new EntitySavingException();
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

    public Optional<Driver> findByTaxiLicencePlate(String licencePlate) {
        Optional<Taxi> byLicencePlate = taxiService.findByLicencePlate(licencePlate);

        return byLicencePlate
                .flatMap(taxi -> driverDao.findByTaxiId(taxi.getId().orElse(-1L))
                                          .map(this::buildDriver));
    }

    private Driver buildDriver(Driver driver) {
        return driver
                .withBuberUser(userService.findById(driver.getId().orElse(-1L))
                                          .orElse(driver.getUser()))
                .withTaxi(taxiService.findById(driver.getTaxi().getId().orElse(-1L))
                                     .orElse(null));
    }

    @Override
    public Driver update(Driver driver) {
        BuberUser user = userService.update(driver.getUser());

        try {
            Driver update = driverDao.update(driver);

            return update.withBuberUser(user)
                         .withTaxi(taxiService.findById(
                                 update.getTaxi().getId().orElse(-1L)).orElse(null)
                         );
        } catch (SQLException e) {
            LOG.error(e);
        }

        return driver;
    }

    @Override
    public boolean delete(Long id) {
        return userService.delete(id);
    }

}
