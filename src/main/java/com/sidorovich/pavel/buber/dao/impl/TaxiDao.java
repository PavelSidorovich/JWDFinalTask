package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.impl.Coordinates;
import com.sidorovich.pavel.buber.model.impl.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class TaxiDao extends CommonDao<Taxi> {

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
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected Set<String> getColumnNames() {
        LinkedHashSet<String> columns = new LinkedHashSet<>();

        columns.add(ID_COLUMN_NAME);
        columns.add(CAR_BRAND_COLUMN_NAME);
        columns.add(CAR_MODEL_COLUMN_NAME);
        columns.add(LICENSE_PLATE_COLUMN_NAME);
        columns.add(LAST_COORDINATES_ID_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Taxi taxi) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(CAR_BRAND_COLUMN_NAME, taxi.getCarBrand());
        map.put(CAR_MODEL_COLUMN_NAME, taxi.getCarModel());
        map.put(LICENSE_PLATE_COLUMN_NAME, taxi.getLicensePlate());
        map.put(LAST_COORDINATES_ID_COLUMN_NAME, taxi.getLastCoordinates().getId()
                                                     .orElseThrow(IdIsNotDefinedException::new));
        return map;
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
