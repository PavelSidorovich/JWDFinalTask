package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.impl.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public final class AccountDao extends CommonDao<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountDao.class);

    private static final String TABLE_NAME = "account";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".id";
    private static final String PHONE_COLUMN_NAME = TABLE_NAME + ".phone";
    private static final String PASSWORD_HASH_COLUMN_NAME = TABLE_NAME + ".password_hash";
    private static final String ROLE_ID_COLUMN_NAME = TABLE_NAME + ".role_id";

    public AccountDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
    }

    @Override
    public boolean create(Account account) {
        String[] columnsToBeInserted = Arrays.copyOfRange(getColumnNames(), 1, getColumnNames().length);
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, columnsToBeInserted)
                               .values(account.getPhone(),
                                       account.getPasswordHash(),
                                       account.getRole().getId().toString())
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(Account account) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(
                                       PHONE_COLUMN_NAME, account.getPhone(),
                                       PASSWORD_HASH_COLUMN_NAME, account.getPasswordHash(),
                                       ROLE_ID_COLUMN_NAME, account.getRole().getId().toString()
                               )
                               .where(ID_COLUMN_NAME,
                                      account.getId().orElseThrow(IdIsNotDefinedException::new).toString())
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
                PHONE_COLUMN_NAME,
                PASSWORD_HASH_COLUMN_NAME,
                ROLE_ID_COLUMN_NAME
        };
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
                Role.getRoleById(rs.getInt(ROLE_ID_COLUMN_NAME))
                    .orElse(Role.CLIENT)
        );
    }
}
