package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.impl.Bonus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class BonusDao extends CommonDao<Bonus> {

    private static final Logger LOG = LogManager.getLogger(BonusDao.class);

    private static final String TABLE_NAME = "bonus";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String CLIENT_ID_COLUMN_NAME = TABLE_NAME + ".client_id";
    private static final String DISCOUNT_COLUMN_NAME = TABLE_NAME + ".discount";
    private static final String EXPIRES_ID_COLUMN_NAME = TABLE_NAME + ".expires";

    public BonusDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
    }

    @Override
    public boolean create(Bonus bonus) {
        String[] columnsToBeInserted = Arrays.copyOfRange(getColumnNames(), 1, getColumnNames().length);
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, columnsToBeInserted)
                               .values(
                                       bonus.getClientId().toString(),
                                       bonus.getDiscount().toString(),
                                       bonus.getExpires().toString()
                               )
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(Bonus bonus) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(CLIENT_ID_COLUMN_NAME, bonus.getClientId().toString(),
                                    DISCOUNT_COLUMN_NAME, bonus.getDiscount().toString(),
                                    EXPIRES_ID_COLUMN_NAME, bonus.getExpires().toString()
                               )
                               .where(ID_COLUMN_NAME, bonus.getId()
                                                           .orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("Account id is not defined", e);
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
                DISCOUNT_COLUMN_NAME,
                EXPIRES_ID_COLUMN_NAME
        };
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Bonus extractResult(ResultSet rs) throws SQLException {
        return new Bonus(
                rs.getLong(ID_COLUMN_NAME),
                rs.getLong(CLIENT_ID_COLUMN_NAME),
                rs.getDouble(DISCOUNT_COLUMN_NAME),
                rs.getDate(EXPIRES_ID_COLUMN_NAME)
        );
    }
}
