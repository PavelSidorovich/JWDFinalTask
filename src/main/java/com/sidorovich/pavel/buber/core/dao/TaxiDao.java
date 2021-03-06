package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import com.sidorovich.pavel.buber.api.model.Taxi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TaxiDao extends CommonDao<Taxi> {

    private static final Logger LOG = LogManager.getLogger(TaxiDao.class);

    private static final String TABLE_NAME = "taxi";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String CAR_BRAND_COLUMN_NAME = TABLE_NAME + ".car_brand";
    private static final String CAR_MODEL_COLUMN_NAME = TABLE_NAME + ".car_model";
    private static final String PHOTO_FILEPATH_COLUMN_NAME = TABLE_NAME + ".car_photo_path";
    private static final String LICENCE_PLATE_COLUMN_NAME = TABLE_NAME + ".license_plate";
    private static final String LAST_COORDINATES_ID_COLUMN_NAME = TABLE_NAME + ".last_coordinates_id";

    TaxiDao(ConnectionPool connectionPool, QueryGeneratorFactory queryGeneratorFactory) {
        super(LOG, connectionPool, queryGeneratorFactory);
    }

    public Optional<Taxi> findByLicencePlate(String licencePlate) {
        QueryGenerator queryGenerator = queryGeneratorFactory.of(connectionPool);
        List<Taxi> list = queryGenerator.select(getColumnNames())
                                        .from(getTableName())
                                        .where(LICENCE_PLATE_COLUMN_NAME, licencePlate)
                                        .fetch(this::extractResultCatchingException);

        return list.isEmpty()? Optional.empty() : Optional.of(list.get(0));
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
        columns.add(PHOTO_FILEPATH_COLUMN_NAME);
        columns.add(LICENCE_PLATE_COLUMN_NAME);
        columns.add(LAST_COORDINATES_ID_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Taxi taxi) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(CAR_BRAND_COLUMN_NAME, taxi.getCarBrand());
        map.put(CAR_MODEL_COLUMN_NAME, taxi.getCarModel());
        map.put(PHOTO_FILEPATH_COLUMN_NAME, taxi.getPhotoFilepath());
        map.put(LICENCE_PLATE_COLUMN_NAME, taxi.getLicencePlate());
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
        Coordinates coordinates = new Coordinates(
                rs.getLong(LAST_COORDINATES_ID_COLUMN_NAME),
                null, null
        );

        return new Taxi(
                rs.getLong(ID_COLUMN_NAME),
                rs.getString(CAR_BRAND_COLUMN_NAME),
                rs.getString(CAR_MODEL_COLUMN_NAME),
                rs.getString(LICENCE_PLATE_COLUMN_NAME),
                rs.getString(PHOTO_FILEPATH_COLUMN_NAME),
                coordinates
        );
    }
}
