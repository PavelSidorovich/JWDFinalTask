package com.sidorovich.pavel.buber.api.model;

import java.util.Objects;

public class Taxi extends CommonEntity<Taxi> {

    private final String carBrand;
    private final String carModel;
    private final String licensePlate;
    private Coordinates lastCoordinates;

    public Taxi(Long id, String carBrand, String carModel, String licensePlate,
                Coordinates lastCoordinates) {
        super(id);
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.licensePlate = licensePlate;
        this.lastCoordinates = lastCoordinates;
    }

    public Taxi(String carBrand, String carModel, String licensePlate,
                Coordinates lastCoordinates) {
        this(null, carBrand, carModel, licensePlate, lastCoordinates);
    }

    @Override
    public Taxi withId(Long id) {
        return new Taxi(id, carBrand, carModel, licensePlate, lastCoordinates);
    }

    public void setLastCoordinates(Coordinates lastCoordinates) {
        this.lastCoordinates = lastCoordinates;
    }

    public Coordinates getLastCoordinates() {
        return lastCoordinates;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Taxi taxi = (Taxi) o;
        return Objects.equals(id, taxi.id) && Objects.equals(carBrand, taxi.carBrand) &&
               Objects.equals(carModel, taxi.carModel) &&
               Objects.equals(licensePlate, taxi.licensePlate) &&
               Objects.equals(lastCoordinates, taxi.lastCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carBrand, carModel, licensePlate, lastCoordinates);
    }

    @Override
    public String toString() {
        return "Taxi{" +
               "id=" + id +
               ", carBrand='" + carBrand + '\'' +
               ", carModel='" + carModel + '\'' +
               ", licensePlate='" + licensePlate + '\'' +
               ", lastCoordinates=" + lastCoordinates +
               '}';
    }


}
