package com.sidorovich.pavel.buber.core.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.AccountDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.*;

public class AccountService implements EntityService<Account> {

    private static final Logger LOG = LogManager.getLogger(AccountService.class);

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

    @Override
    public Account save(Account account) throws SQLException {
        final char[] rawPassword = account.getPasswordHash().toCharArray();
        final String hashedPassword = hasher.hashToString(MIN_COST, rawPassword);

        return accountDao.save(account.withPasswordHash(hashedPassword));
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
        final char[] rawPassword = account.getPasswordHash().toCharArray();
        final String hashedPassword = hasher.hashToString(MIN_COST, rawPassword);

        return accountDao.update(account.withPasswordHash(hashedPassword));
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

}
