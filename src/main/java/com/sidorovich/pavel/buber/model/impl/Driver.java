package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.DriverStatus;

import java.util.Objects;

public class Driver extends BuberUser {

    private final String driverLicense;
    private final Taxi taxi;
    private DriverStatus driverStatus;

    // can be created only using builder
    Driver(BuberUser buberUser, String driverLicense,
           Taxi taxi, DriverStatus driverStatus) {
        super(new Account(buberUser.getId().orElse(null),
                          buberUser.getPhone(), buberUser.getPasswordHash(),
                          buberUser.getRole()
              ), buberUser.getFirstName(), buberUser.getLastName(),
              buberUser.getEmail().orElse(null), buberUser.getCash(),
              buberUser.getStatus());
        this.driverLicense = driverLicense;
        this.taxi = taxi;
        this.driverStatus = driverStatus;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
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
        return Objects.equals(driverLicense, driver.driverLicense) &&
               Objects.equals(taxi, driver.taxi) && driverStatus == driver.driverStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), driverLicense, taxi, driverStatus);
    }
}
