package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class DrivingLicenceValidatorTest {

    private final DrivingLicenceValidator licenceValidator = DrivingLicenceValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenLicenceIsInvalid() {
        Map<String, String> actual = licenceValidator.validate("invalid", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("drivingLicence", "Valid driver licence is required (should be like '1VE 255555')");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenLicenceIsValid() {
        Map<String, String> actual = licenceValidator.validate("1FE 455222", rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}