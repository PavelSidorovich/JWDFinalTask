package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.model.impl.Bonus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BonusDao implements GenericDao<Bonus> {

    private static final Logger LOG = LogManager.getLogger(BonusDao.class);

    private static final String ID_COLUMN_NAME = "id";
    private static final String CLIENT_ID_COLUMN_NAME = "client_id";
    private static final String DISCOUNT_COLUMN_NAME = "discount";
    private static final String EXPIRES_ID_COLUMN_NAME = "expires";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s FROM buber.bonus",
                                                               ID_COLUMN_NAME, CLIENT_ID_COLUMN_NAME,
                                                               DISCOUNT_COLUMN_NAME, EXPIRES_ID_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.bonus (%s, %s, %s) VALUES ('%s', '%s', '%s')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.bonus SET %s = '%s'" + WHERE_CLAUSE_SQL;
    private static final String DELETE_SQL = "DELETE FROM buber.bonus" + WHERE_CLAUSE_SQL;
    private static final String ADDITIONAL_WHERE_CLAUSE_SQL = " AND %s = '%s' AND %s = '%s'";

    @Override
    public boolean create(Bonus bonus) throws InterruptedException {
        String sql = String.format(
                INSERT_SQL, CLIENT_ID_COLUMN_NAME, DISCOUNT_COLUMN_NAME, EXPIRES_ID_COLUMN_NAME,
                bonus.getClientId(), bonus.getDiscount(), bonus.getExpires()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<Bonus> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractBonus);
    }

    @Override
    public Optional<Bonus> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<Bonus> bonuses = SQL_EXECUTOR.executeStatement(sql, this::extractBonus);
        if (bonuses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bonuses.get(0));
    }

    @Override
    public Optional<Bonus> read(String uniqueProperty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Long id, Bonus bonus) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL, DISCOUNT_COLUMN_NAME, bonus.getDiscount(),
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
    public boolean delete(Bonus bonus) throws InterruptedException {
        String sql = String.format(
                DELETE_SQL + ADDITIONAL_WHERE_CLAUSE_SQL,
                CLIENT_ID_COLUMN_NAME, bonus.getClientId(),
                DISCOUNT_COLUMN_NAME, bonus.getDiscount(),
                EXPIRES_ID_COLUMN_NAME, bonus.getExpires()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    private Bonus extractBonus(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return new Bonus(
                    resultSet.getLong(ID_COLUMN_NAME),
                    resultSet.getLong(CLIENT_ID_COLUMN_NAME),
                    resultSet.getDouble(DISCOUNT_COLUMN_NAME),
                    resultSet.getDate(EXPIRES_ID_COLUMN_NAME)
            );
        } catch (SQLException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract bonus");
        }
    }
}
