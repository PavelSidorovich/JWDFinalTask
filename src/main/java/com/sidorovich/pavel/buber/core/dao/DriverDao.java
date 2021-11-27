package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.DriverStatus;
import com.sidorovich.pavel.buber.api.model.Taxi;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
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
    private static final String DRIVER_STATUS_NAME_COLUMN_NAME = TABLE_NAME + ".status_name";

    DriverDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
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
        columns.add(DRIVER_STATUS_NAME_COLUMN_NAME);

        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Driver driver) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(ID_COLUMN_NAME, driver.getUser().getId().orElseThrow(IdIsNotDefinedException::new));
        map.put(DRIVER_LICENSE_COLUMN_NAME, driver.getDrivingLicence());
        map.put(TAXI_ID_COLUMN_NAME, driver.getTaxi().getId()
                                           .orElseThrow(IdIsNotDefinedException::new));
        map.put(DRIVER_STATUS_NAME_COLUMN_NAME, driver.getDriverStatus().name());

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

        return new Driver(
                user, rs.getString(DRIVER_LICENSE_COLUMN_NAME),
                taxi, DriverStatus.getStatusByName(rs.getString(DRIVER_STATUS_NAME_COLUMN_NAME))
        );
    }

    private BuberUser getBuberUser(ResultSet resultSet) throws SQLException {
        return BuberUser.with()
                        .account(
                                new Account(
                                        resultSet.getLong(ID_COLUMN_NAME),
                                        null, null, null
                                )
                        )
                        .build();
    }

    private Taxi getTaxi(ResultSet resultSet) throws SQLException {
        return new Taxi(
                resultSet.getLong(TAXI_ID_COLUMN_NAME), null,
                null, null, null, null
        );
    }

}
