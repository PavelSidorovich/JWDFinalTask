package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.exception.NoSuchCoordinatesException;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import com.sidorovich.pavel.buber.model.impl.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TaxiDao implements GenericDao<Taxi> {

    private static final Logger LOG = LogManager.getLogger(TaxiDao.class);

    private static final String ID_COLUMN_NAME = "id";
    private static final String CAR_BRAND_COLUMN_NAME = "car_brand";
    private static final String CAR_MODEL_COLUMN_NAME = "car_model";
    private static final String LICENSE_PLATE_COLUMN_NAME = "license_plate";
    private static final String LAST_COORDINATES_ID_COLUMN_NAME = "last_coordinates_id";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s, %s FROM buber.taxi",
                                                               ID_COLUMN_NAME, CAR_BRAND_COLUMN_NAME,
                                                               CAR_MODEL_COLUMN_NAME, LICENSE_PLATE_COLUMN_NAME,
                                                               LAST_COORDINATES_ID_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.taxi (%s, %s, %s, %s) VALUES ('%s', '%s', '%s', '%s')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.taxi SET %s = '%s', %s = '%s'" + WHERE_CLAUSE_SQL;
    private static final String DELETE_SQL = "DELETE FROM buber.taxi" + WHERE_CLAUSE_SQL;

    @Override
    public boolean create(Taxi taxi) throws InterruptedException {
        String sql = String.format(
                INSERT_SQL, CAR_BRAND_COLUMN_NAME, CAR_MODEL_COLUMN_NAME,
                LICENSE_PLATE_COLUMN_NAME, LAST_COORDINATES_ID_COLUMN_NAME,
                taxi.getCarBrand(), taxi.getCarModel(), taxi.getLicensePlate(),
                taxi.getLastCoordinates().getId().orElseThrow(NoSuchCoordinatesException::new)
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<Taxi> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractTaxi);
    }

    @Override
    public Optional<Taxi> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<Taxi> taxis = SQL_EXECUTOR.executeStatement(sql, this::extractTaxi);
        return taxis.isEmpty()? Optional.empty() : Optional.of(taxis.get(0));
    }

    @Override
    public Optional<Taxi> read(String licensePlate) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, LICENSE_PLATE_COLUMN_NAME, licensePlate);
        List<Taxi> taxis = SQL_EXECUTOR.executeStatement(sql, this::extractTaxi);
        return taxis.isEmpty()? Optional.empty() : Optional.of(taxis.get(0));
    }

    @Override
    public boolean update(Long id, Taxi taxi) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL,
                LICENSE_PLATE_COLUMN_NAME, taxi.getLicensePlate(),
                LAST_COORDINATES_ID_COLUMN_NAME, taxi.getLastCoordinates().getId()
                                                     .orElseThrow(IdIsNotDefinedException::new),
                ID_COLUMN_NAME, id
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Long id) throws InterruptedException {
        String sql = String.format(DELETE_SQL, ID_COLUMN_NAME, id);
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Taxi taxi) throws InterruptedException {
        String sql = String.format(
                DELETE_SQL,
                LICENSE_PLATE_COLUMN_NAME, taxi.getLicensePlate()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    private Taxi extractTaxi(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            CoordinatesDao coordinatesDao = new CoordinatesDao();
            Coordinates coordinates = coordinatesDao.read(resultSet.getLong(LAST_COORDINATES_ID_COLUMN_NAME))
                                                    .orElseThrow(IdIsNotDefinedException::new);
            return new Taxi(
                    resultSet.getLong(ID_COLUMN_NAME),
                    resultSet.getString(CAR_BRAND_COLUMN_NAME),
                    resultSet.getString(CAR_MODEL_COLUMN_NAME),
                    resultSet.getString(LICENSE_PLATE_COLUMN_NAME),
                    coordinates
            );
        } catch (SQLException | InterruptedException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract taxi");
        }
    }
}
