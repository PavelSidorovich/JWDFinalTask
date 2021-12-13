package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class CashValidatorTest {

    private final CashValidator cashValidator = CashValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenCashIsInvalid() {
        Map<String, String> actual = cashValidator.validate("10,4", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("cash", "Valid cash value is required (for example, 500.00)");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenCashIsValid() {
        Map<String, String> actual = cashValidator.validate("10.4", rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}