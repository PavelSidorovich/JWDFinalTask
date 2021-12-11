package com.sidorovich.pavel.buber.api.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class BuberUser implements Entity<BuberUser> {

    private final Account account;
    private final String firstName;
    private final String lastName;
    private final String email; // can be null
    private final UserStatus status;
    private final BigDecimal cash;

    /* can be created only using builder */
    private BuberUser(Account account, String firstName, String lastName,
                      String email, BigDecimal cash, UserStatus status) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cash = cash;
        this.status = status;
    }

    @Override
    public Optional<Long> getId() {
        return account.getId();
    }

    @Override
    public BuberUser withId(Long id) {
        return new BuberUser(
                account.withId(id), firstName,
                lastName, email, cash, status
        );
    }

    public BuberUser withAccount(Account account) {
        return new BuberUser(
                account, firstName,
                lastName, email, cash, status
        );
    }

    public BuberUser withEmail(String email) {
        return new BuberUser(account, firstName, lastName, email, cash, status);
    }

    public BuberUser withCash(BigDecimal cash) {
        return new BuberUser(
                account, firstName,
                lastName, email, cash, status
        );
    }

    public BuberUser withStatus(UserStatus status) {
        return new BuberUser(
                account, firstName,
                lastName, email, cash, status
        );
    }

    public Account getAccount() {
        return new Account(account.getId().orElse(null),
                           account.getPhone(),
                           account.getPasswordHash(),
                           account.getRole());
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

    public UserStatus getStatus() {
        return status;
    }

    public BigDecimal getCash() {
        return cash;
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
        BuberUser buberUser = (BuberUser) o;
        return Objects.equals(account, buberUser.account) &&
               Objects.equals(firstName, buberUser.firstName) &&
               Objects.equals(lastName, buberUser.lastName) && Objects.equals(email, buberUser.email) &&
               status == buberUser.status && Objects.equals(cash, buberUser.cash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, firstName, lastName, email, status, cash);
    }

    @Override
    public String toString() {
        return "{" +
               "\"id\":\"" + account.getId().orElse(-1L) + "\"" +
               ", \"phone\":\"" + account.getPhone() + "\"" +
               ", \"passwordHash\":\"" + account.getPasswordHash() + "\"" +
               ", \"role\":\"" + account.getRole() + "\"" +
               ", \"firstName\":\"" + firstName + "\"" +
               ", \"lastName\":\"" + lastName + "\"" +
               ", \"email\":\"" + email + "\"" +
               ", \"cash\":\"" + cash + "\"" +
               ", \"status\":\"" + status + "\"" +
               '}';
    }

    public static UserBuilder with() {
        return new UserBuilder();
    }

    public static class UserBuilder implements Builder<BuberUser> {

        private Account account;
        private String firstName;
        private String lastName;
        private String email;
        private UserStatus status;
        private BigDecimal cash;

        private UserBuilder() {
        }

        public UserBuilder account(Account account) {
            this.account = account;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public UserBuilder cash(BigDecimal cash) {
            this.cash = cash;
            return this;
        }

        @Override
        public void reset() {
            account = null;
            firstName = null;
            lastName = null;
            email = null;
            status = null;
            cash = null;
        }

        @Override
        public BuberUser build() {
            return new BuberUser(
                    account, firstName, lastName,
                    email, cash, status
            );
        }
    }

}
