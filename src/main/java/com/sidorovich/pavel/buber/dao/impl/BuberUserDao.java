package com.sidorovich.pavel.buber.dao.impl;

import com.sidorovich.pavel.buber.db.ConnectionPool;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.model.UserStatus;
import com.sidorovich.pavel.buber.model.impl.Account;
import com.sidorovich.pavel.buber.model.impl.BuberUser;
import com.sidorovich.pavel.buber.model.impl.BuberUserBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BuberUserDao extends CommonDao<BuberUser> {

    private static final Logger LOG = LogManager.getLogger(BuberUserDao.class);

    private static final String TABLE_NAME = "buber_user";
    private static final String TABLE_NAME_WITH_DB = DATABASE_NAME + "." + TABLE_NAME;
    private static final String ID_COLUMN_NAME = TABLE_NAME + ".user_id";
    private static final String FIRST_NAME_COLUMN_NAME = TABLE_NAME + ".first_name";
    private static final String LAST_NAME_COLUMN_NAME = TABLE_NAME + ".last_name";
    private static final String EMAIL_COLUMN_NAME = TABLE_NAME + ".email";
    private static final String MONEY_COLUMN_NAME = TABLE_NAME + ".money";
    private static final String STATUS_ID_COLUMN_NAME = TABLE_NAME + ".status_id";

    private final AccountDao accountDao;

    public BuberUserDao(ConnectionPool connectionPool) {
        super(LOG, connectionPool);
        accountDao = new AccountDao(connectionPool);
    }

    @Override
    public boolean create(BuberUser user) {
        try {
            return sqlGenerator.insertInto(TABLE_NAME_WITH_DB, getColumnNames())
                               .values(
                                       user.getId()
                                           .orElseThrow(IdIsNotDefinedException::new).toString(),
                                       user.getFirstName(),
                                       user.getLastName(),
                                       user.getEmail().orElse(""),
                                       user.getCash().toString(),
                                       user.getStatus().getId().toString()
                               )
                               .executeUpdate() == 1;
        } catch (InterruptedException e) {
            LOG.warn("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean update(BuberUser user) {
        try {
            return sqlGenerator.update(TABLE_NAME_WITH_DB)
                               .set(FIRST_NAME_COLUMN_NAME, user.getFirstName(),
                                    LAST_NAME_COLUMN_NAME, user.getLastName(),
                                    EMAIL_COLUMN_NAME, user.getEmail().orElse(""),
                                    MONEY_COLUMN_NAME, user.getCash().toString(),
                                    STATUS_ID_COLUMN_NAME, user.getStatus().getId().toString()
                               )
                               .where(ID_COLUMN_NAME, user.getId()
                                                          .orElseThrow(IdIsNotDefinedException::new).toString())
                               .executeUpdate() == 1;
        } catch (IdIsNotDefinedException e) {
            LOG.warn("BuberUser id is not defined", e);
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
                FIRST_NAME_COLUMN_NAME,
                LAST_NAME_COLUMN_NAME,
                EMAIL_COLUMN_NAME,
                MONEY_COLUMN_NAME,
                STATUS_ID_COLUMN_NAME
        };
    }

    @Override
    protected String getPrimaryColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    protected BuberUser extractResult(ResultSet rs) throws SQLException {
        Optional<Account> optionalAccount = accountDao.read(rs.getLong(ID_COLUMN_NAME));
        Account account = optionalAccount.orElseThrow(IdIsNotDefinedException::new);
        return buildBuberUser(rs, account);
    }

    private BuberUser buildBuberUser(ResultSet resultSet, Account account) throws SQLException {
        return new BuberUserBuilder()
                .setAccount(account)
                .setFirstName(resultSet.getString(FIRST_NAME_COLUMN_NAME))
                .setLastName(resultSet.getString(LAST_NAME_COLUMN_NAME))
                .setEmail(resultSet.getString(EMAIL_COLUMN_NAME))
                .setCash(resultSet.getBigDecimal(MONEY_COLUMN_NAME))
                .setStatus(UserStatus.getStatusById(resultSet.getInt(STATUS_ID_COLUMN_NAME))
                                     .orElse(UserStatus.ACTIVE))
                .getResult();
    }
}
