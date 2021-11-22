package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.CoordinatesDao;
import com.sidorovich.pavel.buber.core.dao.TaxiDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxiService implements EntityService<Taxi> {

    private final TaxiDao taxiDao;
    private final CoordinatesDao coordinatesDao;

    public TaxiService(TaxiDao taxiDao, CoordinatesDao coordinatesDao) {
        this.taxiDao = taxiDao;
        this.coordinatesDao = coordinatesDao;
    }

    // TODO: 11/21/2021 make transactional
    @Override
    public Taxi save(Taxi taxi) throws SQLException {
        Coordinates savedCoordinates = coordinatesDao.save(taxi.getLastCoordinates());

        try {
            return taxiDao.save(taxi).withLastCoordinates(savedCoordinates);
        } catch (SQLException e) {
            coordinatesDao.delete(savedCoordinates.getId().orElse(-1L));
            throw e;
        }
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

    // TODO: 11/21/2021 make transactional
    @Override
    public Taxi update(Taxi taxi) {
        Coordinates updatedCoordinates = coordinatesDao.update(taxi.getLastCoordinates());
        Taxi updatedTaxi = taxiDao.update(taxi);

        return updatedTaxi.withLastCoordinates(updatedCoordinates);
    }

    @Override
    public boolean delete(Long id) {
        return taxiDao.delete(id);
    }

}
