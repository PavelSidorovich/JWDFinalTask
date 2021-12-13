package com.sidorovich.pavel.buber.core.dto;

import com.sidorovich.pavel.buber.api.model.Coordinates;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class OrderDtoTest {

    private final OrderDto orderDto = new OrderDto(
            "+375 29 570-82-79",
            "23",
            "24",
            "34",
            "35",
            "1314 DD-4",
            "20"
    );

    @Test
    public void getPhone_shouldReturnPhone_always() {
        assertEquals(orderDto.getPhone(), "+375 29 570-82-79");
    }

    @Test
    public void getInitialCoordinates_shouldReturnBonus_whenInitialCoordinatesAreValid() {
        assertEquals(orderDto.getInitialCoordinates(), new Coordinates(
                new BigDecimal(24), new BigDecimal(23))
        );
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void getInitialCoordinates_shouldThrowNumberFormatException_whenInitialCoordinatesAreInvalid() {
        new OrderDto(
                "+375 29 570-82-79",
                "wgwg",
                "wgwg",
                "31",
                "45",
                "1314 DD-4",
                "wgg"
        );
    }

    @Test
    public void getEndCoordinates_shouldReturnBonus_whenEndCoordinatesAreValid() {
        assertEquals(orderDto.getEndCoordinates(), new Coordinates(
                new BigDecimal(35), new BigDecimal(34))
        );
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void getEndCoordinates_shouldThrowNumberFormatException_whenEndCoordinatesAreInvalid() {
        new OrderDto(
                "+375 29 570-82-79",
                "52",
                "52",
                "2gw",
                "wgwg",
                "1314 DD-4",
                "wgg"
        );
    }

    @Test
    public void getLicencePlate_shouldReturnLicencePlate_always() {
        assertEquals(orderDto.getLicencePlate(), "1314 DD-4");
    }

    @Test
    public void getBonus_shouldReturnBonus_whenBonusIsValid() {
        assertEquals(orderDto.getBonus(), 20d, 2);
    }

    @Test
    public void getBonus_shouldReturnZero_whenBonusIsInvalid() {
        final OrderDto invalid = new OrderDto(
                "+375 29 570-82-79",
                "23",
                "24",
                "34",
                "35",
                "1314 DD-4",
                "wgg"
        );

        assertEquals(invalid.getBonus(), 0d, 2);
    }

}