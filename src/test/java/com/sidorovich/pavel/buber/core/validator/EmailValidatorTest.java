package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class EmailValidatorTest {

    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenEmailIsInvalid() {
        Map<String, String> actualOptional = emailValidator.validate("invalid", true, rs);
        Map<String, String> actualSimple = emailValidator.validate("invalid", false, rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("email", "Valid email address is required");

        assertEquals(actualOptional, expected);
        assertEquals(actualSimple, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenEmailIsValid() {
        Map<String, String> actualOptionalEmpty = emailValidator.validate("", true, rs);
        Map<String, String> actualOptional = emailValidator.validate("example@mail.ru", true, rs);
        Map<String, String> actualSimple = emailValidator.validate("example@mail.ru", false, rs);

        assertEquals(actualOptionalEmpty, Collections.EMPTY_MAP);
        assertEquals(actualOptional, Collections.EMPTY_MAP);
        assertEquals(actualSimple, Collections.EMPTY_MAP);
    }

}