package com.sidorovich.pavel.buber.core.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.AccountDao;
import com.sidorovich.pavel.buber.api.exception.DuplicateKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.*;

public class AccountService implements EntityService<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountService.class);

    private static final String USER_ALREADY_EXISTS_MSG = "User with this phone already exists";
    private static final String PHONE_PARAM_NAME = "phone";
    private static final byte[] DUMMY_PASSWORD = "password".getBytes(StandardCharsets.UTF_8);

    private final AccountDao accountDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifier;

    public AccountService(AccountDao accountDao, Hasher hasher,
                          Verifyer verifier) {
        this.accountDao = accountDao;
        this.hasher = hasher;
        this.verifier = verifier;
    }

    public Account updatePassword(Account account) {
        final char[] rawPassword = account.getPasswordHash().toCharArray();
        final String hashedPassword = hasher.hashToString(MIN_COST, rawPassword);

        try {
            return accountDao.update(account.withPasswordHash(hashedPassword));
        } catch (SQLException e) {
            LOG.error(e);
        }

        return account;
    }

    @Override
    public Account save(Account account) throws DuplicateKeyException {
        final char[] rawPassword = account.getPasswordHash().toCharArray();
        final String hashedPassword = hasher.hashToString(MIN_COST, rawPassword);

        try {
            return accountDao.save(account.withPasswordHash(hashedPassword));
        } catch (SQLException e) {
            throw new DuplicateKeyException(PHONE_PARAM_NAME, USER_ALREADY_EXISTS_MSG);
        }
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountDao.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public Account update(Account account) {
        try {
            return accountDao.update(account);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return account;
    }

    @Override
    public boolean delete(Long id) {
        return accountDao.delete(id);
    }

    public Optional<Account> authenticate(String phone, String password) {
        LOG.trace("authenticating user");
        if (phone == null || password == null) {
            return Optional.empty();
        }
        final byte[] enteredPassword = password.getBytes(StandardCharsets.UTF_8);
        final Optional<Account> readAccount = accountDao.readAccountByPhone(phone);
        if (readAccount.isPresent()) {
            final byte[] hashedPassword = readAccount.get()
                                                     .getPasswordHash()
                                                     .getBytes(StandardCharsets.UTF_8);
            return verifier.verify(enteredPassword, hashedPassword).verified
                    ? readAccount
                    : Optional.empty();
        } else {
            protectFromTimingAttack(enteredPassword);
            return Optional.empty();
        }
    }

    private void protectFromTimingAttack(byte[] enteredPassword) {
        verifier.verify(enteredPassword, DUMMY_PASSWORD);
    }

    public Optional<Account> readAccountByPhone(String phone) {
        return accountDao.readAccountByPhone(phone);
    }

}
