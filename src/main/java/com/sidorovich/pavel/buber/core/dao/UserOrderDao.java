package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.api.model.UserOrderBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class UserOrderDao extends CommonDao<UserOrder> {

    private static final Logger LOG = LogManager.getLogger(UserOrderDao.class);

    private static final String TABLE_NAME = "order";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String CLIENT_ID_COLUMN_NAME = TABLE_NAME + ".client_id";
    private static final String DRIVER_ID_COLUMN_NAME = TABLE_NAME + ".driver_id";
    private static final String PRICE_COLUMN_NAME = TABLE_NAME + ".price";
    private static final String INITIAL_COORDINATES_ID_COLUMN_NAME = TABLE_NAME + ".initial_coordinates_id";
    private static final String END_COORDINATES_ID_COLUMN_NAME = TABLE_NAME + ".end_coordinates_id";
    private static final String STATUS_ID_COLUMN_NAME = TABLE_NAME + ".status_id";

    private final DriverDao driverDao;
    private final BuberUserDao buberUserDao;
    private final CoordinatesDao coordinatesDao;

    public UserOrderDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
        driverDao = new DriverDao(connectionPool);
        buberUserDao = new BuberUserDao(connectionPool);
        coordinatesDao = new CoordinatesDao(connectionPool);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected Set<String> getColumnNames() {
        LinkedHashSet<String> columns = new LinkedHashSet<>();

        columns.add(ID_COLUMN_NAME);
        columns.add(CLIENT_ID_COLUMN_NAME);
        columns.add(DRIVER_ID_COLUMN_NAME);
        columns.add(PRICE_COLUMN_NAME);
        columns.add(INITIAL_COORDINATES_ID_COLUMN_NAME);
        columns.add(END_COORDINATES_ID_COLUMN_NAME);
        columns.add(STATUS_ID_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(UserOrder order) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(CLIENT_ID_COLUMN_NAME, order.getClient().getId()
                                            .orElseThrow(IdIsNotDefinedException::new));
        map.put(DRIVER_ID_COLUMN_NAME, order.getDriver().getId()
                                            .orElseThrow(IdIsNotDefinedException::new));
        map.put(PRICE_COLUMN_NAME, order.getPrice());
        map.put(INITIAL_COORDINATES_ID_COLUMN_NAME, order.getInitialCoordinates().getId()
                                                         .orElseThrow(IdIsNotDefinedException::new));
        map.put(END_COORDINATES_ID_COLUMN_NAME, order.getEndCoordinates().getId()
                                                     .orElseThrow(IdIsNotDefinedException::new));
        map.put(STATUS_ID_COLUMN_NAME, order.getStatus().getId());
        return map;
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected UserOrder extractResult(ResultSet rs) throws SQLException {
        UserOrderBuilder orderBuilder = new UserOrderBuilder();
        return getUserOrder(rs, orderBuilder);
    }

    private UserOrder getUserOrder(ResultSet resultSet, UserOrderBuilder orderBuilder)
            throws SQLException {
        Coordinates initial = getInitialCoordinates(resultSet);
        Coordinates end = getEndCoordinates(resultSet);
        Driver driver = getDriver(resultSet);
        BuberUser client = getClient(resultSet);
        return orderBuilder.setId(resultSet.getLong(ID_COLUMN_NAME))
                           .setPrice(resultSet.getBigDecimal(PRICE_COLUMN_NAME))
                           .setStatus(OrderStatus.getStatusById(resultSet.getLong(STATUS_ID_COLUMN_NAME))
                                                 .orElseThrow(IdIsNotDefinedException::new))
                           .setClient(client)
                           .setDriver(driver)
                           .setInitialCoordinates(initial)
                           .setEndCoordinates(end)
                           .getResult();
    }

    private BuberUser getClient(ResultSet resultSet) throws SQLException {
        return buberUserDao.read(resultSet.getLong(CLIENT_ID_COLUMN_NAME))
                           .orElseThrow(IdIsNotDefinedException::new);
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        return driverDao.read(resultSet.getLong(DRIVER_ID_COLUMN_NAME))
                        .orElseThrow(IdIsNotDefinedException::new);
    }

    private Coordinates getEndCoordinates(ResultSet resultSet) throws SQLException {
        return coordinatesDao.read(resultSet.getLong(END_COORDINATES_ID_COLUMN_NAME))
                             .orElseThrow(IdIsNotDefinedException::new);
    }

    private Coordinates getInitialCoordinates(ResultSet resultSet) throws SQLException {
        return coordinatesDao.read(resultSet.getLong(INITIAL_COORDINATES_ID_COLUMN_NAME))
                             .orElseThrow(IdIsNotDefinedException::new);
    }
}
