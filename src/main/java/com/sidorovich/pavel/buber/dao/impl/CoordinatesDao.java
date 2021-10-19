package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CoordinatesDao extends CommonDao<Coordinates> {

    private static final Logger LOG = LogManager.getLogger(CoordinatesDao.class);

    private static final String TABLE_NAME = "coordinate";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String LATITUDE_COLUMN_NAME = TABLE_NAME + ".latitude";
    private static final String LONGITUDE_COLUMN_NAME = TABLE_NAME + ".longitude";

    public CoordinatesDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
    }

    @Override
    public boolean create(Coordinates coordinates) {
        String[] columnsToBeInserted = Arrays.copyOfRange(getColumnNames(), 1, getColumnNames().length);
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, columnsToBeInserted)
                               .values(
                                       coordinates.getLatitude().toString(),
                                       coordinates.getLongitude().toString()
                               )
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(Coordinates coordinates) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(LATITUDE_COLUMN_NAME, coordinates.getLatitude().toString(),
                                    LONGITUDE_COLUMN_NAME, coordinates.getLongitude().toString()
                               )
                               .where(ID_COLUMN_NAME,
                                      coordinates.getId().orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("Coordinates id is not defined", e);
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
                LATITUDE_COLUMN_NAME,
                LONGITUDE_COLUMN_NAME
        };
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Coordinates extractResult(ResultSet rs) throws SQLException {
        return new Coordinates(
                rs.getLong(ID_COLUMN_NAME),
                rs.getBigDecimal(LATITUDE_COLUMN_NAME),
                rs.getBigDecimal(LONGITUDE_COLUMN_NAME)
        );
    }
}
