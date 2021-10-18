package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.CannotBuildUserOrderException;
import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.exception.NoSuchOrderStatusException;
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
import java.util.List;
import java.util.Optional;

public class UserOrderDao implements GenericDao<UserOrder> {

    private static final Logger LOG = LogManager.getLogger(UserOrderDao.class);

    private static final DriverDao DRIVER_DAO = new DriverDao();
    private static final BuberUserDao BUBER_USER_DAO = new BuberUserDao();
    private static final CoordinatesDao COORDINATES_DAO = new CoordinatesDao();

    private static final String ID_COLUMN_NAME = "id";
    private static final String CLIENT_ID_COLUMN_NAME = "client_id";
    private static final String DRIVER_ID_COLUMN_NAME = "driver_id";
    private static final String PRICE_COLUMN_NAME = "price";
    private static final String INITIAL_COORDINATES_ID_COLUMN_NAME = "initial_coordinates_id";
    private static final String END_COORDINATES_ID_COLUMN_NAME = "end_coordinates_id";
    private static final String STATUS_ID_COLUMN_NAME = "status_id";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM buber.order",
                                                               ID_COLUMN_NAME, CLIENT_ID_COLUMN_NAME,
                                                               DRIVER_ID_COLUMN_NAME, PRICE_COLUMN_NAME,
                                                               INITIAL_COORDINATES_ID_COLUMN_NAME,
                                                               END_COORDINATES_ID_COLUMN_NAME,
                                                               STATUS_ID_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.order (%s, %s, %s, %s, %s, %s)" +
                                             " VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.order SET %s = '%s', %s = '%s'" + WHERE_CLAUSE_SQL;
    private static final String DELETE_SQL = "DELETE FROM buber.order" + WHERE_CLAUSE_SQL;

    @Override
    public boolean create(UserOrder order) throws InterruptedException {
        String sql = String.format(
                INSERT_SQL, CLIENT_ID_COLUMN_NAME, DRIVER_ID_COLUMN_NAME,
                PRICE_COLUMN_NAME, INITIAL_COORDINATES_ID_COLUMN_NAME,
                END_COORDINATES_ID_COLUMN_NAME, STATUS_ID_COLUMN_NAME,
                order.getClient().getId().orElseThrow(IdIsNotDefinedException::new),
                order.getDriver().getId().orElseThrow(IdIsNotDefinedException::new),
                order.getPrice(),
                order.getInitialCoordinates().getId().orElseThrow(IdIsNotDefinedException::new),
                order.getEndCoordinates().getId().orElseThrow(IdIsNotDefinedException::new),
                order.getStatus().getId()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<UserOrder> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractOrder);
    }

    @Override
    public Optional<UserOrder> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<UserOrder> orders = SQL_EXECUTOR.executeStatement(sql, this::extractOrder);
        return orders.isEmpty()? Optional.empty() : Optional.of(orders.get(0));
    }

    @Override
    public Optional<UserOrder> read(String uniqueProperty) {
        return Optional.empty();
    }

    @Override
    public boolean update(Long id, UserOrder order) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL,
                PRICE_COLUMN_NAME, order.getPrice(),
                STATUS_ID_COLUMN_NAME, order.getStatus().getId(),
                ID_COLUMN_NAME, id
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Long id) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(UserOrder order) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    private UserOrder extractOrder(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            UserOrderBuilder orderBuilder = UserOrderBuilder.getInstance();
            Optional<UserOrder> userOrder = getUserOrder(resultSet, orderBuilder);
            return userOrder.orElseThrow(CannotBuildUserOrderException::new);
        } catch (SQLException | InterruptedException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract user order");
        }
    }

    private Optional<UserOrder> getUserOrder(ResultSet resultSet, UserOrderBuilder orderBuilder)
            throws SQLException, InterruptedException {
        Coordinates initial = getInitialCoordinates(resultSet);
        Coordinates end = getEndCoordinates(resultSet);
        Driver driver = getDriver(resultSet);
        BuberUser client = getClient(resultSet);
        return orderBuilder.setId(resultSet.getLong(ID_COLUMN_NAME))
                           .setPrice(resultSet.getBigDecimal(PRICE_COLUMN_NAME))
                           .setStatus(OrderStatus.getStatusById(resultSet.getLong(STATUS_ID_COLUMN_NAME))
                                                 .orElseThrow(NoSuchOrderStatusException::new))
                           .setClient(client)
                           .setDriver(driver)
                           .setInitialCoordinates(initial)
                           .setEndCoordinates(end)
                           .getResult();
    }

    private BuberUser getClient(ResultSet resultSet) throws InterruptedException, SQLException {
        return BUBER_USER_DAO.read(resultSet.getLong(CLIENT_ID_COLUMN_NAME))
                             .orElseThrow(IdIsNotDefinedException::new);
    }

    private Driver getDriver(ResultSet resultSet) throws InterruptedException, SQLException {
        return DRIVER_DAO.read(resultSet.getLong(DELETE_SQL))
                         .orElseThrow(IdIsNotDefinedException::new);
    }

    private Coordinates getEndCoordinates(ResultSet resultSet) throws InterruptedException, SQLException {
        return COORDINATES_DAO.read(resultSet.getLong(END_COORDINATES_ID_COLUMN_NAME))
                              .orElseThrow(IdIsNotDefinedException::new);
    }

    private Coordinates getInitialCoordinates(ResultSet resultSet) throws InterruptedException, SQLException {
        return COORDINATES_DAO.read(resultSet.getLong(INITIAL_COORDINATES_ID_COLUMN_NAME))
                              .orElseThrow(IdIsNotDefinedException::new);
    }
}
