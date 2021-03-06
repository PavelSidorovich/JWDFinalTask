package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BonusDao extends CommonDao<Bonus> {

    private static final Logger LOG = LogManager.getLogger(BonusDao.class);

    private static final String TABLE_NAME = "bonus";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String CLIENT_ID_COLUMN_NAME = TABLE_NAME + ".client_id";
    private static final String DISCOUNT_COLUMN_NAME = TABLE_NAME + ".discount";
    private static final String EXPIRES_COLUMN_NAME = TABLE_NAME + ".expires";

    BonusDao(ConnectionPool connectionPool, QueryGeneratorFactory queryGeneratorFactory) {
        super(LOG, connectionPool, queryGeneratorFactory);
    }

    public List<Bonus> findBonusesByUserId(Long id) {
        QueryGenerator queryGenerator = queryGeneratorFactory.of(connectionPool);

        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .where(CLIENT_ID_COLUMN_NAME, id)
                             .fetch(this::extractResultCatchingException);
    }

    public List<Bonus> findBonusesByUserIdAndDiscount(Long id, Double discount) {
        QueryGenerator queryGenerator = queryGeneratorFactory.of(connectionPool);

        return queryGenerator.select(getColumnNames())
                             .from(getTableName())
                             .where(CLIENT_ID_COLUMN_NAME, id)
                             .and(DISCOUNT_COLUMN_NAME, discount)
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
        columns.add(DISCOUNT_COLUMN_NAME);
        columns.add(EXPIRES_COLUMN_NAME);
        columns.add(CLIENT_ID_COLUMN_NAME);

        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Bonus bonus) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(DISCOUNT_COLUMN_NAME, bonus.getDiscount());
        map.put(EXPIRES_COLUMN_NAME, bonus.getExpireDate());
        map.put(CLIENT_ID_COLUMN_NAME, bonus.getClient().getId().orElse(-1L));

        return map;
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Bonus extractResult(ResultSet rs) throws SQLException {
        return new Bonus(
                rs.getLong(ID_COLUMN_NAME),
                rs.getDouble(DISCOUNT_COLUMN_NAME),
                rs.getDate(EXPIRES_COLUMN_NAME),
                BuberUser.with()
                         .account(new Account(
                                 rs.getLong(CLIENT_ID_COLUMN_NAME), null,
                                 null, null)
                         ).build()
        );
    }
}
