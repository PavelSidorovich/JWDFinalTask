package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class BuberUser extends Account {

    private final String firstName;
    private final String lastName;
    private String email; // can be null
    private BigDecimal cash;
    private final UserStatus status;

    // can be created only using builder
    BuberUser(Account account, String firstName, String lastName,
              String email, BigDecimal cash, UserStatus status) {
        super(account.getId().orElse(null), account.getPhone(),
              account.getPasswordHash(), account.getRole());
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

    public void setEmail(String email) {
        this.email = email;
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
               "id=" + getId() +
               ", phone='" + getPhone() + '\'' +
               ", passwordHash='" + getPasswordHash() + '\'' +
               ", role=" + getRole() +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", cash=" + cash +
               ", status=" + status +
               '}';
    }


}
