package com.sidorovich.pavel.buber.api.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Role {
    ADMIN("admin", 1),
    DRIVER("driver", 2),
    CLIENT("client", 3),
    UNAUTHORIZED("unauthorized", 4);

    private static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    private final String name;
    private final int id;

    Role(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    public static Optional<Role> getRoleById(int id) {
        return Arrays.stream(values())
                     .filter(role -> role.id == id)
                     .findAny();
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
