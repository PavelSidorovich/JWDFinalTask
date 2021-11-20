package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.model.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class CoordinatesDao extends CommonDao<Coordinates> {

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
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected Set<String> getColumnNames() {
        LinkedHashSet<String> columns = new LinkedHashSet<>();

        columns.add(ID_COLUMN_NAME);
        columns.add(LATITUDE_COLUMN_NAME);
        columns.add(LONGITUDE_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Coordinates coordinates) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(LATITUDE_COLUMN_NAME, coordinates.getLatitude());
        map.put(LONGITUDE_COLUMN_NAME, coordinates.getLongitude());
        return map;
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
