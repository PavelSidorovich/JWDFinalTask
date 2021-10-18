package com.sidorovich.pavel.buber.dao;

import com.sidorovich.pavel.buber.exception.EntityExtractionFailedException;
import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.impl.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountDao implements GenericDao<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountDao.class);

    private static final String ID_COLUMN_NAME = "id";
    private static final String PHONE_COLUMN_NAME = "phone";
    private static final String PASSWORD_HASH_COLUMN_NAME = "password_hash";
    private static final String ROLE_ID_COLUMN_NAME = "role_id";

    private static final String SELECT_ALL_SQL = String.format("SELECT %s, %s, %s, %s FROM buber.account",
                                                               ID_COLUMN_NAME, PHONE_COLUMN_NAME,
                                                               PASSWORD_HASH_COLUMN_NAME, ROLE_ID_COLUMN_NAME);
    private static final String INSERT_SQL = "INSERT INTO buber.account (%s, %s, %s) VALUES ('%s', '%s', '%d')";
    private static final String WHERE_CLAUSE_SQL = " WHERE %s = '%s'";
    private static final String UPDATE_SQL = "UPDATE buber.account SET %s = '%s'" + WHERE_CLAUSE_SQL;

    @Override
    public boolean create(Account account) throws InterruptedException {
        String sql = String.format(
                INSERT_SQL, PHONE_COLUMN_NAME, PASSWORD_HASH_COLUMN_NAME, ROLE_ID_COLUMN_NAME,
                account.getPhone(), account.getPasswordHash(), account.getRole().getId()
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public List<Account> readAll() throws InterruptedException {
        return SQL_EXECUTOR.executeStatement(SELECT_ALL_SQL, this::extractAccount);
    }

    @Override
    public Optional<Account> read(Long id) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, ID_COLUMN_NAME, id);
        List<Account> accounts = SQL_EXECUTOR.executeStatement(sql, this::extractAccount);
        return accounts.isEmpty()? Optional.empty() : Optional.of(accounts.get(0));
    }

    @Override
    public Optional<Account> read(String phone) throws InterruptedException {
        String sql = String.format(SELECT_ALL_SQL + WHERE_CLAUSE_SQL, PHONE_COLUMN_NAME, phone);
        List<Account> accounts = SQL_EXECUTOR.executeStatement(sql, this::extractAccount);
        return accounts.isEmpty()? Optional.empty() : Optional.of(accounts.get(0));
    }

    @Override
    public boolean update(Long id, Account account) throws InterruptedException {
        String sql = String.format(
                UPDATE_SQL, PASSWORD_HASH_COLUMN_NAME, account.getPasswordHash(),
                ID_COLUMN_NAME, id
        );
        return SQL_EXECUTOR.executeUpdate(sql) == 1;
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Account account) {
        throw new UnsupportedOperationException();
    }

    private Account extractAccount(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return new Account(
                    resultSet.getLong(ID_COLUMN_NAME),
                    resultSet.getString(PHONE_COLUMN_NAME),
                    resultSet.getString(PASSWORD_HASH_COLUMN_NAME),
                    Role.getRoleById(resultSet.getInt(ROLE_ID_COLUMN_NAME))
                        .orElse(Role.CLIENT)
            );
        } catch (SQLException e) {
            LOG.error("Could not extract value from result set", e);
            throw new EntityExtractionFailedException("Failed to extract account");
        }
    }

}
