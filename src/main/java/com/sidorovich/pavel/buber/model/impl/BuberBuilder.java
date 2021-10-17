package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Singleton
 */
public class BuberBuilder implements Builder<BuberUser> {

    private Integer id;
    private String phone;
    private String passwordHash;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal cash;
    private UserStatus status;

    private BuberBuilder() {
    }

    private static class InstanceCreator {
        static BuberBuilder INSTANCE = new BuberBuilder();
    }

    public static BuberBuilder getInstance() {
        return BuberBuilder.InstanceCreator.INSTANCE;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Automatically calls reset() method
     *
     * @return optional value of BuberUser
     */
    @Override
    public Optional<BuberUser> getResult() {
        if (phone == null || passwordHash == null || role == null ||
            firstName == null || lastName == null || cash == null ||
            status == null) {
            reset();
            return Optional.empty();
        }
        Optional<BuberUser> result = Optional.of(
                new BuberUser(
                        id, phone, passwordHash,
                        role, firstName, lastName,
                        email, cash, status
                )
        );
        reset();
        return result;
    }

    @Override
    public void reset() {
        this.id = null;
        this.phone = null;
        this.passwordHash = null;
        this.role = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.cash = null;
        this.status = null;
    }
}
