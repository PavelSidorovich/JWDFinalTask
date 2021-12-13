package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Taxi;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class CarNameValidatorTest {

    private final CarNameValidator carNameValidator = CarNameValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenCarNameIsInvalid() {
        Map<String, String> actual = carNameValidator.validate(
                new Taxi("Beetle'14", "Long''2", null, null, null), rs
        );
        HashMap<String, String> expected = new HashMap<>();

        expected.put("carBrand", "Valid car brand is required");
        expected.put("carModel", "Valid car model is required");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenCarNameIsValid() {
        Map<String, String> actual = carNameValidator.validate(
                new Taxi("Beetle", "Long", null, null, null), rs
        );

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}