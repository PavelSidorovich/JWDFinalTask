package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.UserDao;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.exception.EntitySavingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService implements EntityService<BuberUser> {

    private static final Logger LOG = LogManager.getLogger(UserService.class);

    private final UserDao userDao;
    private final AccountService accountService;

    UserService(UserDao userDao, AccountService accountService) {
        this.userDao = userDao;
        this.accountService = accountService;
    }

    // TODO: 11/21/2021 make transactional
    @Override
    public BuberUser save(BuberUser user) throws DuplicateKeyException {
        Account account = accountService.save(user.getAccount());
        BuberUser buberUserWithId = user.withAccount(account);

        try {
            return userDao.save(buberUserWithId).withAccount(account);
        } catch (SQLException e) {
            accountService.delete(account.getId().orElse(-1L));
            throw new EntitySavingException();
        }
    }

    @Override
    public Optional<BuberUser> findById(Long id) {
        return userDao.findById(id)
                      .map(this::buildUser);
    }

    public Optional<BuberUser> findByPhone(String phone) {
        Optional<Account> account = accountService.readAccountByPhone(phone);

        return account.flatMap(value -> userDao.findById(value.getId().orElse(-1L))
                                               .map(this::buildUser));
    }

    @Override
    public List<BuberUser> findAll() {
        return userDao.findAll().stream()
                      .map(this::buildUser)
                      .collect(Collectors.toList());
    }

    private BuberUser buildUser(BuberUser user) {
        return user.withAccount(
                accountService.findById(user.getId().orElse(-1L))
                              .orElse(user.getAccount())
        );
    }

    // TODO: 11/21/2021 make transactional
    @Override
    public BuberUser update(BuberUser user) {
        try {
            Account updatedAccount = accountService.update(user.getAccount());
            BuberUser updatedUser = userDao.update(user);

            return updatedUser.withAccount(updatedAccount);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return user;
    }

    public BuberUser updatePassword(BuberUser user) {
        try {
            Account updatedAccount = accountService.updatePassword(user.getAccount());
            BuberUser updatedUser = userDao.update(user);

            return updatedUser.withAccount(updatedAccount);
        } catch (SQLException e) {
            LOG.error(e);
        }

        return user;
    }

    @Override
    public boolean delete(Long id) {
        return accountService.delete(id);
    }


}
