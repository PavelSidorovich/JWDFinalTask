package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.User;

import java.util.Objects;
import java.util.Optional;

public class Account implements User {

    private Long id; // can be null
    private final String phone;
    private final String passwordHash;
    private final Role role;

    public Account(Long id, String phone, String passwordHash, Role role) {
        this.id = id;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Account(String phone, String passwordHash, Role role) {
        this(null, phone, passwordHash, role);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }

    public String getPhone() {
        return phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(phone, account.phone) &&
               Objects.equals(passwordHash, account.passwordHash) && role == account.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, passwordHash, role);
    }
}
