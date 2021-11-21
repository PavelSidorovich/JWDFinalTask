package com.sidorovich.pavel.buber.api.model;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN("admin"),
    DRIVER("driver"),
    CLIENT("client"),
    UNAUTHORIZED("unauthorized");

    private static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    public static Role getRoleByName(String roleName) {
        return valueOf(roleName.toUpperCase());
    }

    public String getRoleName() {
        return roleName;
    }

}
