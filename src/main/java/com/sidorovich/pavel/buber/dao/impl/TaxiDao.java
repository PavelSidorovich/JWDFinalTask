package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import com.sidorovich.pavel.buber.model.impl.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class TaxiDao extends CommonDao<Taxi> {

    private static final Logger LOG = LogManager.getLogger(TaxiDao.class);

    private static final String TABLE_NAME = "taxi";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String CAR_BRAND_COLUMN_NAME = TABLE_NAME + ".car_brand";
    private static final String CAR_MODEL_COLUMN_NAME = TABLE_NAME + ".car_model";
    private static final String LICENSE_PLATE_COLUMN_NAME = TABLE_NAME + ".license_plate";
    private static final String LAST_COORDINATES_ID_COLUMN_NAME = TABLE_NAME + ".last_coordinates_id";

    private final CoordinatesDao coordinatesDao;

    public TaxiDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
        coordinatesDao = new CoordinatesDao(connectionPool);
    }

    @Override
    public boolean create(Taxi taxi) {
        String[] columnsToBeInserted = Arrays.copyOfRange(getColumnNames(), 1, getColumnNames().length);
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, columnsToBeInserted)
                               .values(
                                       taxi.getCarBrand(),
                                       taxi.getCarModel(),
                                       taxi.getLicensePlate(),
                                       taxi.getLastCoordinates().getId()
                                           .orElseThrow(IdIsNotDefinedException::new).toString()
                               )
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(Taxi taxi) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(CAR_BRAND_COLUMN_NAME, taxi.getCarBrand(),
                                    CAR_MODEL_COLUMN_NAME, taxi.getCarModel(),
                                    LICENSE_PLATE_COLUMN_NAME, taxi.getLicensePlate(),
                                    LAST_COORDINATES_ID_COLUMN_NAME,
                                    taxi.getLastCoordinates().getId()
                                        .orElseThrow(IdIsNotDefinedException::new).toString()
                               )
                               .where(ID_COLUMN_NAME, taxi.getId()
                                                          .orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("Taxi id is not defined", e);
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
                CAR_BRAND_COLUMN_NAME,
                CAR_MODEL_COLUMN_NAME,
                LICENSE_PLATE_COLUMN_NAME,
                LAST_COORDINATES_ID_COLUMN_NAME
        };
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Taxi extractResult(ResultSet rs) throws SQLException {
        Coordinates coordinates = coordinatesDao.read(rs.getLong(LAST_COORDINATES_ID_COLUMN_NAME))
                                                .orElseThrow(IdIsNotDefinedException::new);
        return new Taxi(
                rs.getLong(ID_COLUMN_NAME),
                rs.getString(CAR_BRAND_COLUMN_NAME),
                rs.getString(CAR_MODEL_COLUMN_NAME),
                rs.getString(LICENSE_PLATE_COLUMN_NAME),
                coordinates
        );
    }
}
