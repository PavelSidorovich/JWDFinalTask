package com.sidorovich.pavel.buber.model;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN("admin", 1),
    DRIVER("driver", 2),
    CLIENT("client", 3);

    private final String name;
    private final int id;

    Role(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Optional<Role> getRoleById(int id) {
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
