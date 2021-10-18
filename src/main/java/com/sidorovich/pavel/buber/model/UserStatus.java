package com.sidorovich.pavel.buber.model;

import java.util.Arrays;
import java.util.Optional;

public enum UserStatus {
    ACTIVE("active", 1),
    BLOCKED("blocked", 2);

    private final String name;
    private final int id;

    UserStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Optional<UserStatus> getStatusById(int id) {
        return Arrays.stream(values())
                     .filter(role -> role.id == id)
                     .findAny();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
