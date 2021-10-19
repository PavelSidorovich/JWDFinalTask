package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.exception.BuilderNullFieldsException;
import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;

public class BuberUserBuilder implements Builder<BuberUserBuilder, BuberUser> {

    private Account account;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal cash;
    private UserStatus status;

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
     * @return BuberUser entity
     */
    @Override
    public BuberUser getResult() {
        if (account == null || firstName == null ||
            lastName == null || cash == null || status == null) {
            reset();
            throw new BuilderNullFieldsException();
        }
        BuberUser buberUser = new BuberUser(account, firstName, lastName,
                                            email, cash, status);
        reset();
        return buberUser;
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
