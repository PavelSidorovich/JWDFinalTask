package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class PhoneValidatorTest {

    private final PhoneValidator validator = PhoneValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenPhoneIsInvalid() {
        Map<String, String> actual = validator.validate("invalid", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("phone", "Valid phone is required");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenPhoneIsValid() {
        Map<String, String> actual = validator.validate("+375 29 456-55-22", rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}