package com.sidorovich.pavel.buber.api.model;

public enum DriverStatus {
    BUSY("busy"),
    FREE("free"),
    PENDING("pending"),
    REJECTED("rejected");

    private final String statusName;

    DriverStatus(String statusName) {
        this.statusName = statusName;
    }

    public static DriverStatus getStatusByName(String statusName) {
        return valueOf(statusName.toUpperCase());
    }

    public String getStatusName() {
        return statusName;
    }

}
