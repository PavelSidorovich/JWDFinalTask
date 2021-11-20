package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverBuilder;
import com.sidorovich.pavel.buber.api.model.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class DriverDao extends CommonDao<Driver> {

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
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected Set<String> getColumnNames() {
        LinkedHashSet<String> columns = new LinkedHashSet<>();

        columns.add(ID_COLUMN_NAME);
        columns.add(DRIVER_LICENSE_COLUMN_NAME);
        columns.add(TAXI_ID_COLUMN_NAME);
        columns.add(DRIVER_STATUS_ID_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Driver driver) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(DRIVER_LICENSE_COLUMN_NAME, driver.getDriverLicense());
        map.put(TAXI_ID_COLUMN_NAME, driver.getTaxi().getId()
                                           .orElseThrow(IdIsNotDefinedException::new));
        map.put(DRIVER_STATUS_ID_COLUMN_NAME, driver.getDriverStatus().getId());
        return map;
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
