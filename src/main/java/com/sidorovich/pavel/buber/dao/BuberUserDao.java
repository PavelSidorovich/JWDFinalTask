package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.exception.IdIsNotDefinedException;
import com.sidorovich.pavel.buber.exception.NoSuchAccountException;
import com.sidorovich.pavel.buber.model.UserStatus;
import com.sidorovich.pavel.buber.model.impl.Account;
import com.sidorovich.pavel.buber.model.impl.BuberUser;
import com.sidorovich.pavel.buber.model.impl.BuberUserBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BuberUserDao implements GenericDao<BuberUser> {

    private static final Logger LOG = LogManager.getLogger(BuberUserDao.class);

    private static final AccountDao ACCOUNT_DAO = new AccountDao();

    private static final String ID_COLUMN_NAME = "user_id";
    private static final String FIRST_NAME_COLUMN_NAME = "first_name";
    private static final String LAST_NAME_COLUMN_NAME = "last_name";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String MONEY_COLUMN_NAME = "money";
    private static final String STATUS_ID_COLUMN_NAME = "status_id";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s, %s, %s FROM buber.buber_user",
                                                               ID_COLUMN_NAME, FIRST_NAME_COLUMN_NAME,
                                                               LAST_NAME_COLUMN_NAME, EMAIL_COLUMN_NAME,
                                                               MONEY_COLUMN_NAME, STATUS_ID_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.buber_user (%s, %s, %s, %s, %s, %s) " +
                                             "VALUES ('%s', '%s', '%s', '%s', '%f', '%d')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.buber_user SET %s = '%s', %s = '%s', %s = '%s'," +
                                             " %s = '%s', %s = '%s'" + WHERE_CLAUSE_SQL;

    @Override
    public boolean create(BuberUser user) throws InterruptedException {
        ACCOUNT_DAO.create(user);
        Account account = ACCOUNT_DAO.read(user.getPhone()).orElseThrow(NoSuchAccountException::new);
        String sql = String.format(
                INSERT_SQL, ID_COLUMN_NAME, FIRST_NAME_COLUMN_NAME, LAST_NAME_COLUMN_NAME,
                EMAIL_COLUMN_NAME, MONEY_COLUMN_NAME, STATUS_ID_COLUMN_NAME,
                account.getId().orElseThrow(IdIsNotDefinedException::new), user.getFirstName(), user.getLastName(),
                user.getEmail().orElse(null), user.getCash(), user.getStatus().getId()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<BuberUser> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractUser);
    }

    @Override
    public Optional<BuberUser> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<BuberUser> users = SQL_EXECUTOR.executeStatement(sql, this::extractUser);
        return users.isEmpty()? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<BuberUser> read(String empty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Long id, BuberUser user) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL,
                FIRST_NAME_COLUMN_NAME, user.getFirstName(),
                LAST_NAME_COLUMN_NAME, user.getLastName(),
                EMAIL_COLUMN_NAME, user.getEmail().orElse(null),
                MONEY_COLUMN_NAME, user.getCash(),
                STATUS_ID_COLUMN_NAME, user.getStatus().getId(),
                ID_COLUMN_NAME, id
        );
        AccountDao accountDao = new AccountDao();
        return accountDao.update(id, user) && SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(BuberUser user) {
        throw new UnsupportedOperationException();
    }

    private BuberUser extractUser(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            Optional<Account> read = ACCOUNT_DAO.read(resultSet.getLong(ID_COLUMN_NAME));
            Account account = read.orElseThrow(NoSuchAccountException::new);
            Optional<BuberUser> result = getBuberUser(resultSet, account);
            return result.orElseThrow(EntityExtractionFailedException::new);
        } catch (SQLException | InterruptedException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract user");
        }
    }

    private Optional<BuberUser> getBuberUser(ResultSet resultSet, Account account) throws SQLException {
        return BuberUserBuilder.getInstance()
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
