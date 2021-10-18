package com.sidorovich.pavel.buber.model.impl;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class Coordinates {

    private Long id;
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public Coordinates(Long id, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this(null, latitude, longitude);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
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
