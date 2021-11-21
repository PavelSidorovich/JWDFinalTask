package com.sidorovich.pavel.buber.api.model;

public enum UserStatus {
    ACTIVE("active"),
    BLOCKED("blocked");

    private final String statusName;

    UserStatus(String statusName) {
        this.statusName = statusName;
    }

    public static UserStatus getStatusByName(String statusName) {
        return valueOf(statusName.toUpperCase());
    }

    public String getStatusName() {
        return statusName;
    }

}
