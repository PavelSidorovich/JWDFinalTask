package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CoordinatesDao implements GenericDao<Coordinates> {

    private static final Logger LOG = LogManager.getLogger(CoordinatesDao.class);

    private static final String ID_COLUMN_NAME = "id";
    private static final String LATITUDE_COLUMN_NAME = "latitude";
    private static final String LONGITUDE_COLUMN_NAME = "longitude";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s FROM buber.coordinate",
                                                               ID_COLUMN_NAME, LATITUDE_COLUMN_NAME,
                                                               LONGITUDE_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.coordinate (%s, %s) VALUES ('%s', '%s')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.coordinate SET %s = '%s', %s = '%s'" + WHERE_CLAUSE_SQL;
    private static final String DELETE_SQL = "DELETE FROM buber.coordinate" + WHERE_CLAUSE_SQL;
    private static final String ADDITIONAL_WHERE_CLAUSE_SQL = " AND %s = '%s'";

    @Override
    public boolean create(Coordinates coordinates) throws InterruptedException {
        String sql = String.format(
                INSERT_SQL, LATITUDE_COLUMN_NAME, LONGITUDE_COLUMN_NAME,
                coordinates.getLatitude(), coordinates.getLongitude()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<Coordinates> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractCoordinates);
    }

    @Override
    public Optional<Coordinates> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<Coordinates> coordinates = SQL_EXECUTOR.executeStatement(sql, this::extractCoordinates);
        return coordinates.isEmpty()? Optional.empty() : Optional.of(coordinates.get(0));
    }

    @Override
    public Optional<Coordinates> read(String uniqueProperty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Long id, Coordinates coordinates) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL,
                LATITUDE_COLUMN_NAME, coordinates.getLatitude(),
                LONGITUDE_COLUMN_NAME, coordinates.getLongitude(),
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
    public boolean delete(Coordinates coordinates) throws InterruptedException {
        String sql = String.format(
                DELETE_SQL + ADDITIONAL_WHERE_CLAUSE_SQL,
                LATITUDE_COLUMN_NAME, coordinates.getLatitude(),
                LONGITUDE_COLUMN_NAME, coordinates.getLongitude()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    private Coordinates extractCoordinates(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return new Coordinates(
                    resultSet.getLong(ID_COLUMN_NAME),
                    resultSet.getBigDecimal(LATITUDE_COLUMN_NAME),
                    resultSet.getBigDecimal(LONGITUDE_COLUMN_NAME)
            );
        } catch (SQLException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract coordinates");
        }
    }
}
