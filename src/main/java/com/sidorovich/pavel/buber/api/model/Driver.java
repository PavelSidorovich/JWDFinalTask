package com.sidorovich.pavel.buber.api.model;

import java.util.Objects;
import java.util.Optional;

public class Driver implements Entity<Driver> {

    private final BuberUser user;
    private final String driverLicense;
    private final Taxi taxi;
    private DriverStatus driverStatus;

    // can be created only using builder
    Driver(BuberUser buberUser, String driverLicense, Taxi taxi, DriverStatus driverStatus) {
        this.user = buberUser;
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

    @Override
    public Optional<Long> getId() {
        return user.getId();
    }

    @Override
    public Driver withId(Long id) {
        return new Driver(user.withId(id), driverLicense, taxi, driverStatus);
    }
}
