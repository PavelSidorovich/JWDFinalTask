package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.model.Bonus;
import com.sidorovich.pavel.buber.api.service.EntityService;
import com.sidorovich.pavel.buber.core.dao.BonusDao;
import com.sidorovich.pavel.buber.exception.DuplicateKeyException;
import com.sidorovich.pavel.buber.exception.EntitySavingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BonusService implements EntityService<Bonus> {

    private static final Logger LOG = LogManager.getLogger(BonusService.class);

    private final BonusDao bonusDao;
    private final UserService userService;

    BonusService(BonusDao bonusDao, UserService userService) {
        this.bonusDao = bonusDao;
        this.userService = userService;
    }

    public List<Bonus> findBonusesByUserId(Long id) {
        return bonusDao.findBonusesByUserId(id).stream()
                       .map(this::buildBonus)
                       .collect(Collectors.toList());
    }

    public List<Bonus> findBonusesByUserIdAndDiscount(Long id, Double discount) {
        return bonusDao.findBonusesByUserIdAndDiscount(id, discount).stream()
                       .map(this::buildBonus)
                       .collect(Collectors.toList());
    }

    @Override
    public Bonus save(Bonus bonus) throws DuplicateKeyException {
        try {
            return bonusDao.save(bonus);
        } catch (SQLException e) {
            throw new EntitySavingException();
        }
    }

    @Override
    public Optional<Bonus> findById(Long id) {
        return bonusDao.findById(id)
                       .map(this::buildBonus);
    }

    @Override
    public List<Bonus> findAll() {
        return bonusDao.findAll().stream()
                       .map(this::buildBonus)
                       .collect(Collectors.toList());
    }

    @Override
    public Bonus update(Bonus bonus) throws DuplicateKeyException {
        try {
            return bonusDao.update(bonus);
        } catch (SQLException e) {
            LOG.error(e);
            return bonus;
        }
    }

    @Override
    public boolean delete(Long id) {
        return bonusDao.delete(id);
    }

    private Bonus buildBonus(Bonus bonus) {
        return new Bonus(
                bonus.getId().orElse(-1L),
                bonus.getDiscount(),
                bonus.getExpireDate(),
                userService.findById(bonus.getClient().getId()
                                          .orElse(-1L)).orElse(null)
        );
    }

}
