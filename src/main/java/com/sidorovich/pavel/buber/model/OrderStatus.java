package com.sidorovich.pavel.buber.model;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    IN_PROCESS("inProgress", 1),
    COMPLETED("completed", 2),
    CANCELLED("cancelled", 3);

    private final String name;
    private final int id;

    OrderStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Optional<OrderStatus> getStatusById(Long id) {
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
