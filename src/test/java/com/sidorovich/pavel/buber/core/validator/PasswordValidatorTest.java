package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = PasswordValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenPasswordsAreNotEqual() {
        Map<String, String> actual = validator.validate("password1", "password2", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("passwordRepeat", "Passwords are not equal");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnErrorMsg_whenPasswordDoesNotMatchesRegex() {
        Map<String, String> actual = validator.validate("123456", "123456", rs);

        HashMap<String, String> expected = new HashMap<>();

        expected.put("password",
                     "Password should contain minimum eight characters, at least one letter and one number");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenPasswordOk() {
        Map<String, String> actual = validator.validate("q1234567", "q1234567", rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}