package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class CarLicencePlateValidatorTest {

    private final CarLicencePlateValidator licencePlateValidator = CarLicencePlateValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenLicencePlateIsInvalid() {
        Map<String, String> actual = licencePlateValidator.validate("24225 AX45", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("licencePlate", "Valid car licence plate is required (should be like '1111 AX-7')");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenLicencePlateValid() {
        Map<String, String> actual = licencePlateValidator.validate("2425 AX-5", rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}