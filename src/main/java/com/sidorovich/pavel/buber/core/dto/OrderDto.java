package com.sidorovich.pavel.buber.core.dto;

import com.sidorovich.pavel.buber.api.model.Coordinates;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderDto {

    private static final String LICENCE_PLATE_REGEX = "\\d{4} [A-Z]{2}-[1-7]";
    private static final String EMPTY_STRING = "";

    private final String phone;
    private final Coordinates initialCoordinates;
    private final Coordinates endCoordinates;
    private final String licencePlate;
    private final double bonus;

    public OrderDto(String phone, String initialLongitude, String initialLatitude,
                    String endLongitude, String endLatitude, String licencePlate, String bonus) {

        this.phone = phone;
        this.initialCoordinates = convertCoordinates(initialLongitude, initialLatitude);
        this.endCoordinates = convertCoordinates(endLongitude, endLatitude);
        this.licencePlate = convertTaxi(licencePlate);
        this.bonus = convertBonus(bonus);
    }

    private Coordinates convertCoordinates(String initialLongitude, String initialLatitude) {
        return new Coordinates(
                new BigDecimal(initialLatitude), new BigDecimal(initialLongitude)
        );
    }

    private double convertBonus(String bonus) {
        try {
            return Double.parseDouble(bonus);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String convertTaxi(String taxi) {
        Matcher matcher = Pattern.compile(LICENCE_PLATE_REGEX).matcher(taxi);

        return matcher.find()? matcher.group() : EMPTY_STRING;
    }

    public String getPhone() {
        return phone;
    }

    public Coordinates getInitialCoordinates() {
        return initialCoordinates;
    }

    public Coordinates getEndCoordinates() {
        return endCoordinates;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDto orderDto = (OrderDto) o;
        return Double.compare(orderDto.bonus, bonus) == 0 && Objects.equals(phone, orderDto.phone) &&
               Objects.equals(initialCoordinates, orderDto.initialCoordinates) &&
               Objects.equals(endCoordinates, orderDto.endCoordinates) &&
               Objects.equals(licencePlate, orderDto.licencePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, initialCoordinates, endCoordinates, licencePlate, bonus);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
               "phone='" + phone + '\'' +
               ", initialCoordinates=" + initialCoordinates +
               ", endCoordinates=" + endCoordinates +
               ", licencePlate='" + licencePlate + '\'' +
               ", bonus=" + bonus +
               '}';
    }

}
