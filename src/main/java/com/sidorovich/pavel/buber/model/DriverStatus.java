package com.sidorovich.pavel.buber.model;

import java.util.Arrays;
import java.util.Optional;

public enum DriverStatus {
    BUSY("busy", 1),
    FREE("free", 2);

    private final String name;
    private final int id;

    DriverStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Optional<DriverStatus> getStatusById(int id) {
        return Arrays.stream(values())
                     .filter(status -> status.id == id)
                     .findAny();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
