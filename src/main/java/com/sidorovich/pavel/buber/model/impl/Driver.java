package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Role;
import com.sidorovich.pavel.buber.model.UserStatus;

import java.math.BigDecimal;
import java.util.Objects;

public class Driver extends BuberUser {

    private final String driverLicense;
    private final Taxi taxi;

    // can be created only using builder
    Driver(Integer id, String phone, String passwordHash, Role role, String firstName, String lastName,
           String email, BigDecimal cash, UserStatus status, String driverLicense,
           Taxi taxi) {
        super(id, phone, passwordHash, role, firstName, lastName, email, cash, status);
        this.driverLicense = driverLicense;
        this.taxi = taxi;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Driver driver = (Driver) o;
        return Objects.equals(driverLicense, driver.driverLicense) && Objects.equals(taxi, driver.taxi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), driverLicense, taxi);
    }
}
