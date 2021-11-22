package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.Account;
import com.sidorovich.pavel.buber.api.model.BuberUser;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.AccountDao;
import com.sidorovich.pavel.buber.core.dao.UserDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService implements EntityService<BuberUser> {

    private final UserDao userDao;
    private final AccountDao accountDao;

    UserService(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    // TODO: 11/21/2021 make transactional
    @Override
    public BuberUser save(BuberUser user) throws SQLException {
        Account account = accountDao.save(user.getAccount());
        BuberUser buberUserWithId = user.withAccount(account);

        try {
            return userDao.save(buberUserWithId).withAccount(account);
        } catch (SQLException e) {
            accountDao.delete(account.getId().orElse(-1L));
            throw e;
        }
    }

    @Override
    public Optional<BuberUser> findById(Long id) {
        return userDao.findById(id)
                      .map(this::buildUser);
    }

    @Override
    public List<BuberUser> findAll() {
        return userDao.findAll().stream()
                      .map(this::buildUser)
                      .collect(Collectors.toList());
    }

    private BuberUser buildUser(BuberUser user) {
        return user.withAccount(
                accountDao.findById(user.getId().orElse(-1L))
                          .orElse(user.getAccount())
        );
    }

    // TODO: 11/21/2021 make transactional
    @Override
    public BuberUser update(BuberUser user) {
        Account updatedAccount = accountDao.update(user.getAccount());
        BuberUser updatedUser = userDao.update(user);

        return updatedUser.withAccount(updatedAccount);
    }

    @Override
    public boolean delete(Long id) {
        return accountDao.delete(id);
    }



}
