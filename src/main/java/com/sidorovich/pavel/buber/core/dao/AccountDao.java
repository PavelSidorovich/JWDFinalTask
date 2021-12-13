package com.sidorovich.pavel.buber.core.dao;

import com.sidorovich.pavel.buber.api.db.ConnectionPool;
import com.sidorovich.pavel.buber.api.db.QueryGenerator;
import com.sidorovich.pavel.buber.api.db.QueryGeneratorFactory;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.Role;
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

public class AccountDao extends CommonDao<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountDao.class);

    private static final String TABLE_NAME = "account";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String PHONE_COLUMN_NAME = TABLE_NAME + ".phone";
    private static final String PASSWORD_HASH_COLUMN_NAME = TABLE_NAME + ".password_hash";
    private static final String ROLE_NAME_COLUMN_NAME = TABLE_NAME + ".role_name";

    AccountDao(ConnectionPool connectionPool,
               QueryGeneratorFactory queryGeneratorFactory) {
        super(LOG, connectionPool, queryGeneratorFactory);
    }

    public Optional<Account> readAccountByPhone(String phone) {
        QueryGenerator queryGenerator = queryGeneratorFactory.of(connectionPool);

        List<Account> list = queryGenerator.select(getColumnNames())
                                           .from(getTableName())
                                           .where(PHONE_COLUMN_NAME, phone)
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
        columns.add(PHONE_COLUMN_NAME);
        columns.add(PASSWORD_HASH_COLUMN_NAME);
        columns.add(ROLE_NAME_COLUMN_NAME);
        return columns;
    }

    @Override
    protected Map<String, Object> getColumnsAndValuesToBeInserted(Account account) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        map.put(PHONE_COLUMN_NAME, account.getPhone());
        map.put(PASSWORD_HASH_COLUMN_NAME, account.getPasswordHash());
        map.put(ROLE_NAME_COLUMN_NAME, account.getRole().name());

        return map;
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected Account extractResult(ResultSet rs) throws SQLException {
        return new Account(
                rs.getLong(ID_COLUMN_NAME),
                rs.getString(PHONE_COLUMN_NAME),
                rs.getString(PASSWORD_HASH_COLUMN_NAME),
                Role.getRoleByName(rs.getString(ROLE_NAME_COLUMN_NAME))
        );
    }

}
