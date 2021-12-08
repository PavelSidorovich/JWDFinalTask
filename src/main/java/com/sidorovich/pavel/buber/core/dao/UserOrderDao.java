package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Driver;
import com.sidorovich.pavel.buber.api.model.OrderStatus;
import com.sidorovich.pavel.buber.api.model.UserOrder;
import com.sidorovich.pavel.buber.core.db.QueryGeneratorImpl;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
    private static final String STATUS_NAME_COLUMN_NAME = TABLE_NAME + ".status_name";
    private static final String DATE_OF_TRIP_COLUMN_NAME = TABLE_NAME + ".date_of_trip";

    UserOrderDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
    }

    public List<UserOrder> findByClientId(Long id) {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .where(CLIENT_ID_COLUMN_NAME, id)
                             .fetch(this::extractResultCatchingException);
    }

    public List<UserOrder> findByDriverId(Long id) {
        QueryGenerator queryGenerator = new QueryGeneratorImpl(connectionPool);

        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .where(DRIVER_ID_COLUMN_NAME, id)
                             .fetch(this::extractResultCatchingException);
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
        columns.add(STATUS_NAME_COLUMN_NAME);
        columns.add(DATE_OF_TRIP_COLUMN_NAME);

        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(UserOrder order) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(CLIENT_ID_COLUMN_NAME, order.getClient().getId()
                                            .orElseThrow(IdIsNotDefinedException::new));
        map.put(DRIVER_ID_COLUMN_NAME, order.getDriver().getId()
                                            .orElse(null));
        map.put(PRICE_COLUMN_NAME, order.getPrice());
        map.put(INITIAL_COORDINATES_ID_COLUMN_NAME, order.getInitialCoordinates().getId()
                                                         .orElseThrow(IdIsNotDefinedException::new));
        map.put(END_COORDINATES_ID_COLUMN_NAME, order.getEndCoordinates().getId()
                                                     .orElseThrow(IdIsNotDefinedException::new));
        map.put(STATUS_NAME_COLUMN_NAME, order.getStatus().name());
        map.put(DATE_OF_TRIP_COLUMN_NAME, order.getDateOfTrip());

        return map;
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected UserOrder extractResult(ResultSet rs) throws SQLException {
        Coordinates initial = getInitialCoordinates(rs);
        Coordinates end = getEndCoordinates(rs);
        Driver driver = getDriver(rs);
        BuberUser client = getClient(rs);

        return UserOrder.with()
                        .id(rs.getLong(ID_COLUMN_NAME))
                        .price(rs.getBigDecimal(PRICE_COLUMN_NAME))
                        .status(OrderStatus.getStatusByName(rs.getString(STATUS_NAME_COLUMN_NAME)))
                        .client(client)
                        .driver(driver)
                        .initialCoordinates(initial)
                        .endCoordinates(end)
                        .dateOfTrip(rs.getDate(DATE_OF_TRIP_COLUMN_NAME))
                        .build();
    }

    private BuberUser getClient(ResultSet resultSet) throws SQLException {
        return BuberUser.with()
                        .account(new Account(resultSet.getLong(CLIENT_ID_COLUMN_NAME),
                                             null, null, null))
                        .build();
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        return new Driver(
                BuberUser.with()
                         .account(new Account(resultSet.getLong(DRIVER_ID_COLUMN_NAME),
                                              null, null, null))
                         .build(),
                null, null, null
        );
    }

    private Coordinates getEndCoordinates(ResultSet resultSet) throws SQLException {
        return new Coordinates(
                resultSet.getLong(END_COORDINATES_ID_COLUMN_NAME),
                null, null
        );
    }

    private Coordinates getInitialCoordinates(ResultSet resultSet) throws SQLException {
        return new Coordinates(
                resultSet.getLong(INITIAL_COORDINATES_ID_COLUMN_NAME),
                null, null
        );
    }
}
