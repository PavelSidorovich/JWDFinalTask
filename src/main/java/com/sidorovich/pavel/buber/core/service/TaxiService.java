package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.CoordinatesDao;
import com.sidorovich.pavel.buber.core.dao.TaxiDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxiService implements EntityService<Taxi> {

    private static final Logger LOG = LogManager.getLogger(TaxiService.class);

    private static final String DUPLICATE_LICENCE_PLATE_MSG = "Car with this licence plate already exists";
    private static final String LICENCE_PLATE_PARAM_NAME = "licencePlate";

    private final TaxiDao taxiDao;
    private final CoordinatesDao coordinatesDao;

    public TaxiService(TaxiDao taxiDao, CoordinatesDao coordinatesDao) {
        this.taxiDao = taxiDao;
        this.coordinatesDao = coordinatesDao;
    }

    @Override
    public Taxi save(Taxi taxi) throws DuplicateKeyException {
        try {
            Coordinates savedCoordinates = coordinatesDao.save(taxi.getLastCoordinates());
            try {
                return taxiDao.save(taxi.withLastCoordinates(savedCoordinates))
                              .withLastCoordinates(savedCoordinates);
            } catch (SQLException e) {
                coordinatesDao.delete(savedCoordinates.getId().orElse(-1L));

                throw new DuplicateKeyException(LICENCE_PLATE_PARAM_NAME, DUPLICATE_LICENCE_PLATE_MSG);
            }
        } catch (SQLException e) {
            LOG.error(e);
            return taxi;
        }
    }

    public Optional<Taxi> findByLicencePlate(String licencePlate) {
        return taxiDao.findByLicencePlate(licencePlate)
                      .map(this::buildTaxi);
    }

    @Override
    public Optional<Taxi> findById(Long id) {
        return taxiDao.findById(id)
                      .map(this::buildTaxi);
    }

    @Override
    public List<Taxi> findAll() {
        return taxiDao.findAll().stream()
                      .map(this::buildTaxi)
                      .collect(Collectors.toList());
    }

    private Taxi buildTaxi(Taxi taxi) {
        return taxi.withLastCoordinates(
                coordinatesDao.findById(taxi.getLastCoordinates().getId().orElse(-1L))
                              .orElse(taxi.getLastCoordinates())
        );
    }

    @Override
    public Taxi update(Taxi taxi) {
        try {
            Coordinates updatedCoordinates = coordinatesDao.update(taxi.getLastCoordinates());
            Taxi updatedTaxi = taxiDao.update(taxi);

            return updatedTaxi.withLastCoordinates(updatedCoordinates);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return taxi;
    }

    @Override
    public boolean delete(Long id) {
        return taxiDao.delete(id);
    }

}
