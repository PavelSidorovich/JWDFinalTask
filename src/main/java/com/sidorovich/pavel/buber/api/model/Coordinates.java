package com.sidorovich.pavel.buber.api.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Coordinates extends CommonEntity<Coordinates> {

    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public Coordinates(Long id, BigDecimal latitude, BigDecimal longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this(null, latitude, longitude);
    }

    @Override
    public Coordinates withId(Long id) {
        return new Coordinates(id, latitude, longitude);
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return Objects.equals(id, that.id) && Objects.equals(latitude, that.latitude) &&
               Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
               "id=" + id +
               ", latitude=" + latitude +
               ", longitude=" + longitude +
               '}';
    }

}
