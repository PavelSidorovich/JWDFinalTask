package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.OrderStatus;
import com.sidorovich.pavel.buber.model.impl.BuberUser;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import com.sidorovich.pavel.buber.model.impl.Driver;
import com.sidorovich.pavel.buber.model.impl.UserOrder;
import com.sidorovich.pavel.buber.model.impl.UserOrderBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserOrderDao extends CommonDao<UserOrder> {

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
    public boolean create(UserOrder order) {
        String[] columnsToBeInserted = Arrays.copyOfRange(getColumnNames(), 1, getColumnNames().length);
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, columnsToBeInserted)
                               .values(order.getClient()
                                            .getId()
                                            .orElseThrow(IdIsNotDefinedException::new).toString(),
                                       order.getDriver()
                                            .getId()
                                            .orElseThrow(IdIsNotDefinedException::new).toString(),
                                       order.getPrice().toString(),
                                       order.getInitialCoordinates()
                                            .getId().orElseThrow(IdIsNotDefinedException::new).toString(),
                                       order.getEndCoordinates()
                                            .getId().orElseThrow(IdIsNotDefinedException::new).toString(),
                                       order.getStatus().getId().toString())
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(UserOrder order) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(CLIENT_ID_COLUMN_NAME, order.getClient().getId()
                                                                .orElseThrow(IdIsNotDefinedException::new)
                                                                .toString(),
                                    DRIVER_ID_COLUMN_NAME, order.getDriver().getId()
                                                                .orElseThrow(IdIsNotDefinedException::new)
                                                                .toString(),
                                    PRICE_COLUMN_NAME, order.getPrice().toString(),
                                    INITIAL_COORDINATES_ID_COLUMN_NAME, order.getInitialCoordinates()
                                                                             .getId()
                                                                             .orElseThrow(IdIsNotDefinedException::new)
                                                                             .toString(),
                                    END_COORDINATES_ID_COLUMN_NAME, order.getEndCoordinates()
                                                                         .getId()
                                                                         .orElseThrow(IdIsNotDefinedException::new)
                                                                         .toString(),
                                    STATUS_ID_COLUMN_NAME, order.getStatus().getId().toString())
                               .where(ID_COLUMN_NAME, order.getId()
                                                           .orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("Order id is not defined", e);
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
                CLIENT_ID_COLUMN_NAME,
                DRIVER_ID_COLUMN_NAME,
                PRICE_COLUMN_NAME,
                INITIAL_COORDINATES_ID_COLUMN_NAME,
                END_COORDINATES_ID_COLUMN_NAME,
                STATUS_ID_COLUMN_NAME
        };
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
