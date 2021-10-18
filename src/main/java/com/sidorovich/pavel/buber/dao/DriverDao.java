package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.exception.NoSuchAccountException;
import com.sidorovich.pavel.buber.exception.NoSuchDriverStatusException;
import com.sidorovich.pavel.buber.model.DriverStatus;
import com.sidorovich.pavel.buber.model.impl.Account;
import com.sidorovich.pavel.buber.model.impl.BuberUser;
import com.sidorovich.pavel.buber.model.impl.Driver;
import com.sidorovich.pavel.buber.model.impl.DriverBuilder;
import com.sidorovich.pavel.buber.model.impl.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// TODO: 10/17/2021
public class DriverDao implements GenericDao<Driver> {

    private static final Logger LOG = LogManager.getLogger(DriverDao.class);

    private static final BuberUserDao BUBER_USER_DAO = new BuberUserDao();
    private static final AccountDao ACCOUNT_DAO = new AccountDao();
    private static final TaxiDao TAXI_DAO = new TaxiDao();

    private static final String ID_COLUMN_NAME = "id";
    private static final String DRIVER_LICENSE_COLUMN_NAME = "driver_license";
    private static final String TAXI_ID_COLUMN_NAME = "taxi_id";
    private static final String DRIVER_STATUS_ID_COLUMN_NAME = "status_id";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s FROM buber.driver",
                                                               ID_COLUMN_NAME, DRIVER_LICENSE_COLUMN_NAME,
                                                               TAXI_ID_COLUMN_NAME, DRIVER_STATUS_ID_COLUMN_NAME);
    private static final String INSERT_SQL =
            "INSERT INTO buber.driver (%s, %s, %s, %s) VALUES ('%s', '%s', '%s', '%s')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.driver SET %s = '%s', %s = '%s', %s = '%s'"
                                             + WHERE_CLAUSE_SQL;

    @Override
    public boolean create(Driver driver) throws InterruptedException {
        BUBER_USER_DAO.create(driver);
        Account account = ACCOUNT_DAO.read(driver.getPhone())
                                     .orElseThrow(NoSuchAccountException::new);
        String sql = String.format(
                INSERT_SQL, ID_COLUMN_NAME, DRIVER_LICENSE_COLUMN_NAME,
                TAXI_ID_COLUMN_NAME, DRIVER_STATUS_ID_COLUMN_NAME,
                account.getId().orElseThrow(IdIsNotDefinedException::new),
                driver.getDriverLicense(),
                driver.getTaxi().getId().orElseThrow(IdIsNotDefinedException::new),
                driver.getDriverStatus().getId()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<Driver> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractDriver);
    }

    @Override
    public Optional<Driver> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<Driver> drivers = SQL_EXECUTOR.executeStatement(sql, this::extractDriver);
        return drivers.isEmpty()? Optional.empty() : Optional.of(drivers.get(0));
    }

    @Override
    public Optional<Driver> read(String driverLicence) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, DRIVER_LICENSE_COLUMN_NAME, driverLicence);
        List<Driver> drivers = SQL_EXECUTOR.executeStatement(sql, this::extractDriver);
        return drivers.isEmpty()? Optional.empty() : Optional.of(drivers.get(0));
    }

    @Override
    public boolean update(Long id, Driver driver) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL,
                DRIVER_LICENSE_COLUMN_NAME, driver.getDriverLicense(),
                TAXI_ID_COLUMN_NAME, driver.getTaxi().getId().orElse(null),
                DRIVER_STATUS_ID_COLUMN_NAME, driver.getDriverStatus().getId(),
                ID_COLUMN_NAME, id
        );
        return BUBER_USER_DAO.update(id, driver) && SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Long id) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Driver driver) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    private Driver extractDriver(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            BuberUser user = getBuberUser(resultSet);
            Taxi taxi = getTaxi(resultSet);
            DriverBuilder builder = DriverBuilder.getInstance();
            Optional<Driver> result = buildDriver(resultSet, user, taxi, builder);
            return result.orElseThrow(EntityExtractionFailedException::new);
        } catch (SQLException | InterruptedException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract taxi");
        }
    }

    private BuberUser getBuberUser(ResultSet resultSet) throws InterruptedException, SQLException {
        return BUBER_USER_DAO.read(resultSet.getLong(ID_COLUMN_NAME))
                             .orElseThrow(IdIsNotDefinedException::new);
    }

    private Taxi getTaxi(ResultSet resultSet) throws InterruptedException, SQLException {
        return TAXI_DAO.read(resultSet.getLong(TAXI_ID_COLUMN_NAME))
                       .orElseThrow(IdIsNotDefinedException::new);
    }

    private Optional<Driver> buildDriver(ResultSet resultSet, BuberUser user, Taxi taxi, DriverBuilder builder)
            throws SQLException {
        return builder
                .setDriverLicense(resultSet.getString(DRIVER_LICENSE_COLUMN_NAME))
                .setDriverStatus(DriverStatus.getStatusById(resultSet.getInt(DRIVER_STATUS_ID_COLUMN_NAME))
                                             .orElseThrow(NoSuchDriverStatusException::new))
                .setBuberUser(user)
                .setTaxi(taxi)
                .getResult();
    }
}
