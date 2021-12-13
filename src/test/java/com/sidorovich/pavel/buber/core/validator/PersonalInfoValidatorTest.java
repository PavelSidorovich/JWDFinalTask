package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.BuberUser;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class PersonalInfoValidatorTest {

    private final PersonalInfoValidator validator = PersonalInfoValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenPersonalInfoIsInvalid() {
        BuberUser user = BuberUser.with().firstName("Pavel4")
                                  .lastName("")
                                  .build();
        Map<String, String> actual = validator.validate(user, rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("lName", "Valid last name is required");
        expected.put("fName", "Valid first name is required");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenPersonalInfoIsValid() {
        BuberUser user = BuberUser.with().firstName("Pavel")
                                  .lastName("Sidorovich")
                                  .build();
        Map<String, String> actual = validator.validate(user, rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}