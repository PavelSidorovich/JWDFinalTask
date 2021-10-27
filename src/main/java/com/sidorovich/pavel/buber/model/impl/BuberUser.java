package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Entity;
import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class BuberUser implements Entity<BuberUser> {

    private final Account account;
    private final String firstName;
    private final String lastName;
    private final String email; // can be null
    private final UserStatus status;
    private BigDecimal cash;

    /* can be created only using builder */
    BuberUser(Account account, String firstName, String lastName,
              String email, BigDecimal cash, UserStatus status) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        if (cash == null) {
            this.cash = BigDecimal.valueOf(0);
        } else {
            this.cash = cash;
        }
        this.status = status;
    }

    public BuberUser withEmail(String email) {
        return new BuberUser(account, firstName, lastName, email, cash, status);
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(this.email);
    }

    public BigDecimal getCash() {
        return cash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Account getAccount() {
        return new Account(account.getId().orElse(null),
                           account.getPhone(),
                           account.getPasswordHash(),
                           account.getRole());
    }

    @Override
    public Optional<Long> getId() {
        return account.getId();
    }

    @Override
    public BuberUser withId(Long id) {
        return new BuberUser(account.withId(id),
                             firstName, lastName,
                             email, cash, status);
    }

    public String getPhone() {
        return account.getPhone();
    }

    public String getPasswordHash() {
        return account.getPasswordHash();
    }

    public Role getRole() {
        return account.getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BuberUser buberUser = (BuberUser) o;
        return Objects.equals(firstName, buberUser.firstName) &&
               Objects.equals(lastName, buberUser.lastName) && Objects.equals(email, buberUser.email) &&
               Objects.equals(cash, buberUser.cash) && status == buberUser.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, cash, status);
    }

    @Override
    public String toString() {
        return "BuberUser{" +
               "id=" + account.getId() +
               ", phone='" + account.getPhone() + '\'' +
               ", passwordHash='" + account.getPasswordHash() + '\'' +
               ", role=" + account.getRole() +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", cash=" + cash +
               ", status=" + status +
               '}';
    }

}
