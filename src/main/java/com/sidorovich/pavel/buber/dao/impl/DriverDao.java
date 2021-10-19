package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.DriverStatus;
import com.sidorovich.pavel.buber.model.impl.BuberUser;
import com.sidorovich.pavel.buber.model.impl.Driver;
import com.sidorovich.pavel.buber.model.impl.DriverBuilder;
import com.sidorovich.pavel.buber.model.impl.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverDao extends CommonDao<Driver> {

    private static final Logger LOG = LogManager.getLogger(DriverDao.class);

    private static final String TABLE_NAME = "driver";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String DRIVER_LICENSE_COLUMN_NAME = TABLE_NAME + ".driver_license";
    private static final String TAXI_ID_COLUMN_NAME = TABLE_NAME + ".taxi_id";
    private static final String DRIVER_STATUS_ID_COLUMN_NAME = TABLE_NAME + ".status_id";

    private final BuberUserDao buberUserDao;
    private final TaxiDao taxiDao;

    public DriverDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
        buberUserDao = new BuberUserDao(connectionPool);
        taxiDao = new TaxiDao(connectionPool);
    }

    @Override
    public boolean create(Driver driver) {
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, getColumnNames())
                               .values(
                                       driver.getId()
                                             .orElseThrow(IdIsNotDefinedException::new).toString(),
                                       driver.getDriverLicense(),
                                       driver.getTaxi().getId()
                                             .orElseThrow(IdIsNotDefinedException::new).toString(),
                                       driver.getDriverStatus().getId().toString()
                               )
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(Driver driver) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(DRIVER_LICENSE_COLUMN_NAME, driver.getDriverLicense(),
                                    TAXI_ID_COLUMN_NAME, driver.getTaxi().getId()
                                                               .orElseThrow(IdIsNotDefinedException::new)
                                                               .toString(),
                                    DRIVER_STATUS_ID_COLUMN_NAME, driver.getStatus().getId().toString()
                               )
                               .where(ID_COLUMN_NAME, driver.getId()
                                                            .orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("Driver id is not defined", e);
            return false;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {
                ID_COLUMN_NAME,
                DRIVER_LICENSE_COLUMN_NAME,
                TAXI_ID_COLUMN_NAME,
                DRIVER_STATUS_ID_COLUMN_NAME
        };
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Driver extractResult(ResultSet rs) throws SQLException {
        BuberUser user = getBuberUser(rs);
        Taxi taxi = getTaxi(rs);
        DriverBuilder builder = new DriverBuilder();
        return buildDriver(rs, user, taxi, builder);
    }

    private BuberUser getBuberUser(ResultSet resultSet) throws SQLException {
        return buberUserDao.read(resultSet.getLong(ID_COLUMN_NAME))
                           .orElseThrow(IdIsNotDefinedException::new);
    }

    private Taxi getTaxi(ResultSet resultSet) throws SQLException {
        return taxiDao.read(resultSet.getLong(TAXI_ID_COLUMN_NAME))
                      .orElseThrow(IdIsNotDefinedException::new);
    }

    private Driver buildDriver(ResultSet resultSet, BuberUser user, Taxi taxi, DriverBuilder builder)
            throws SQLException {
        return builder
                .setDriverLicense(resultSet.getString(DRIVER_LICENSE_COLUMN_NAME))
                .setDriverStatus(DriverStatus.getStatusById(resultSet.getInt(DRIVER_STATUS_ID_COLUMN_NAME))
                                             .orElse(DriverStatus.FREE))
                .setBuberUser(user)
                .setTaxi(taxi)
                .getResult();
    }
}
