package com.sidorovich.pavel.buber.api.model;

import java.util.Objects;
import java.util.Optional;

public class Driver implements Entity<Driver> {

    private final BuberUser user;
    private final String drivingLicence;
    private final Taxi taxi;
    private final DriverStatus driverStatus;

    public Driver(BuberUser buberUser, String drivingLicence, Taxi taxi, DriverStatus driverStatus) {
        this.user = buberUser;
        this.drivingLicence = drivingLicence;
        this.taxi = taxi;
        this.driverStatus = driverStatus;
    }

    @Override
    public Optional<Long> getId() {
        return user.getId();
    }

    @Override
    public Driver withId(Long id) {
        return new Driver(user.withId(id), drivingLicence, taxi, driverStatus);
    }

    public Driver withBuberUser(BuberUser user) {
        return new Driver(user, drivingLicence, taxi, driverStatus);
    }

    public Driver withTaxi(Taxi taxi) {
        return new Driver(user, drivingLicence, taxi, driverStatus);
    }

    public Driver withDriverStatus(DriverStatus driverStatus) {
        return new Driver(user, drivingLicence, taxi, driverStatus);
    }

    public BuberUser getUser() {
        return user;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public static Driver empty() {
        return new Driver(
                BuberUser.with()
                         .account(new Account(null, null,
                                              null, null
                                  )
                         )
                         .build(),
                null, null, null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        return Objects.equals(user, driver.user) &&
               Objects.equals(drivingLicence, driver.drivingLicence) &&
               Objects.equals(taxi, driver.taxi) && driverStatus == driver.driverStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, drivingLicence, taxi, driverStatus);
    }

    @Override
    public String toString() {
        return "Driver{" +
               "user=" + user +
               ", driverLicense='" + drivingLicence + '\'' +
               ", taxi=" + taxi +
               ", driverStatus=" + driverStatus +
               '}';
    }

}
