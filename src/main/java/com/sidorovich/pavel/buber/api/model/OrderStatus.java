package com.sidorovich.pavel.buber.api.model;

public enum OrderStatus {
    IN_PROCESS("in progress"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    public static OrderStatus getStatusByName(String statusName) {
        return valueOf(statusName);
    }

    public String getStatusName() {
        return statusName;
    }

}
