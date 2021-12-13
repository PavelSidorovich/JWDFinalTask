package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.model.UserStatus;
import com.sidorovich.pavel.buber.api.exception.IdIsNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class UserDao extends CommonDao<BuberUser> {

    private static final Logger LOG = LogManager.getLogger(UserDao.class);

    private static final String TABLE_NAME = "buber_user";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".user_id";
    private static final String FIRST_NAME_COLUMN_NAME = TABLE_NAME + ".first_name";
    private static final String LAST_NAME_COLUMN_NAME = TABLE_NAME + ".last_name";
    private static final String EMAIL_COLUMN_NAME = TABLE_NAME + ".email";
    private static final String MONEY_COLUMN_NAME = TABLE_NAME + ".money";
    private static final String STATUS_NAME_COLUMN_NAME = TABLE_NAME + ".status_name";

    UserDao(ConnectionPool connectionPool, QueryGeneratorFactory queryGeneratorFactory) {
        super(LOG, connectionPool, queryGeneratorFactory);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME_WITH_DB;
    }

    @Override
    protected Set<String> getColumnNames() {
        LinkedHashSet<String> columns = new LinkedHashSet<>();

        columns.add(ID_COLUMN_NAME);
        columns.add(FIRST_NAME_COLUMN_NAME);
        columns.add(LAST_NAME_COLUMN_NAME);
        columns.add(EMAIL_COLUMN_NAME);
        columns.add(MONEY_COLUMN_NAME);
        columns.add(STATUS_NAME_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(BuberUser user) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(ID_COLUMN_NAME, user.getAccount().getId().orElseThrow(IdIsNotDefinedException::new));
        map.put(FIRST_NAME_COLUMN_NAME, user.getFirstName());
        map.put(LAST_NAME_COLUMN_NAME, user.getLastName());
        map.put(EMAIL_COLUMN_NAME, user.getEmail().orElse(null));
        map.put(MONEY_COLUMN_NAME, user.getCash());
        map.put(STATUS_NAME_COLUMN_NAME, user.getStatus().name());

        return map;
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected BuberUser extractResult(ResultSet rs) throws SQLException {
        Account account = new Account(rs.getLong(ID_COLUMN_NAME), null, null, null);

        return buildBuberUser(rs, account);
    }

    private BuberUser buildBuberUser(ResultSet resultSet, Account account) throws SQLException {
        return BuberUser.with()
                        .account(account)
                        .firstName(resultSet.getString(FIRST_NAME_COLUMN_NAME))
                        .lastName(resultSet.getString(LAST_NAME_COLUMN_NAME))
                        .email(resultSet.getString(EMAIL_COLUMN_NAME))
                        .cash(resultSet.getBigDecimal(MONEY_COLUMN_NAME))
                        .status(UserStatus.getStatusByName(resultSet.getString(STATUS_NAME_COLUMN_NAME)))
                        .build();
    }
}
