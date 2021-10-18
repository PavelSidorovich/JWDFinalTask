package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Singleton
 */
public class BuberUserBuilder implements Builder<BuberUserBuilder, BuberUser> {

    private Account account;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal cash;
    private UserStatus status;

    private BuberUserBuilder() {
    }

    private static class InstanceCreator {
        static BuberUserBuilder INSTANCE = new BuberUserBuilder();
    }

    public static BuberUserBuilder getInstance() {
        return BuberUserBuilder.InstanceCreator.INSTANCE;
    }

    public BuberUserBuilder setAccount(Account account) {
        this.account = account;
        return this;
    }

    public BuberUserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BuberUserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BuberUserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public BuberUserBuilder setCash(BigDecimal cash) {
        this.cash = cash;
        return this;
    }

    public BuberUserBuilder setStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Automatically calls reset() method
     *
     * @return optional value of BuberUser
     */
    @Override
    public Optional<BuberUser> getResult() {
        if (account == null || firstName == null ||
            lastName == null || cash == null || status == null) {
            reset();
            return Optional.empty();
        }
        Optional<BuberUser> result = Optional.of(
                new BuberUser(
                        account, firstName, lastName,
                        email, cash, status
                )
        );
        reset();
        return result;
    }

    @Override
    public BuberUserBuilder reset() {
        this.account = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.cash = null;
        this.status = null;
        return this;
    }
}
