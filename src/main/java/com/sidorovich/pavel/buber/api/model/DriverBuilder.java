package com.sidorovich.pavel.buber.api.model;

import com.sidorovich.pavel.buber.exception.BuilderNullFieldsException;

public class DriverBuilder implements Builder<DriverBuilder, Driver> {

    private BuberUser buberUser;
    private String driverLicense;
    private Taxi taxi;
    private DriverStatus driverStatus;

    public DriverBuilder setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
        return this;
    }

    public DriverBuilder setBuberUser(BuberUser buberUser) {
        this.buberUser = buberUser;
        return this;
    }


    public DriverBuilder setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
        return this;
    }

    public DriverBuilder setTaxi(Taxi taxi) {
        this.taxi = taxi;
        return this;
    }

    /**
     * Automatically calls reset() method
     *
     * @return optional value of Driver
     */
    @Override
    public Driver getResult() {
        if (buberUser == null || taxi == null ||
            driverLicense == null || driverStatus == null) {
            reset();
            throw new BuilderNullFieldsException();
        }
        Driver driver = new Driver(buberUser, this.driverLicense, this.taxi, this.driverStatus);
        reset();
        return driver;
    }

    @Override
    public DriverBuilder reset() {
        this.buberUser = null;
        this.taxi = null;
        this.driverLicense = null;
        this.driverStatus = null;
        return this;
    }
}
