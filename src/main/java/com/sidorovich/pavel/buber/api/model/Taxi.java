package com.sidorovich.pavel.buber.api.model;

import java.util.Objects;

public class Taxi extends CommonEntity<Taxi> {

    private final String carBrand;
    private final String carModel;
    private final String licencePlate;
    private final String photoFilepath;
    private final Coordinates lastCoordinates;

    public Taxi(Long id, String carBrand, String carModel, String licencePlate, String photoFilepath,
                Coordinates lastCoordinates) {
        super(id);
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.licencePlate = licencePlate;
        this.photoFilepath = photoFilepath;
        this.lastCoordinates = lastCoordinates;
    }

    public Taxi(String carBrand, String carModel, String licencePlate, String photoFilepath,
                Coordinates lastCoordinates) {
        this(null, carBrand, carModel, licencePlate, photoFilepath, lastCoordinates);
    }

    @Override
    public Taxi withId(Long id) {
        return new Taxi(id, carBrand, carModel, licencePlate, photoFilepath, lastCoordinates);
    }

    public Taxi withLastCoordinates(Coordinates lastCoordinates) {
        return new Taxi(id, carBrand, carModel, licencePlate, photoFilepath, lastCoordinates);
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getPhotoFilepath() {
        return photoFilepath;
    }

    public Coordinates getLastCoordinates() {
        return lastCoordinates;
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
        return Objects.equals(carBrand, taxi.carBrand) && Objects.equals(carModel, taxi.carModel) &&
               Objects.equals(licencePlate, taxi.licencePlate) &&
               Objects.equals(photoFilepath, taxi.photoFilepath) &&
               Objects.equals(lastCoordinates, taxi.lastCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carBrand, carModel, licencePlate, photoFilepath, lastCoordinates);
    }

    @Override
    public String toString() {
        return "Taxi{" +
               "id=" + id +
               ", carBrand='" + carBrand + '\'' +
               ", carModel='" + carModel + '\'' +
               ", licensePlate='" + licencePlate + '\'' +
               ", photoFilepath='" + photoFilepath + '\'' +
               ", lastCoordinates=" + lastCoordinates +
               '}';
    }

}
