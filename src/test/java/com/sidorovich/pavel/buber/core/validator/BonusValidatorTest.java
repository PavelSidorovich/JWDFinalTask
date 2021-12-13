package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.model.Bonus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class BonusValidatorTest {

    private final BonusValidator bonusValidator = BonusValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrors_whenBonusIsInvalid() {
        Map<String, String> actual = bonusValidator.validate(
                new Bonus(1L, 120d, Date.valueOf(LocalDate.of(2020, 12, 10)), null),
                rs
        );
        HashMap<String, String> expected = new HashMap<>();

        expected.put("discount", "Discount value should be in range (0..100%)");
        expected.put("expireDate", "Valid date is required");

        assertEquals(actual, expected);
    }

    @Test
    public void validate_shouldReturnEmptyMap_whenBonusValid() {
        Map<String, String> actual = bonusValidator.validate(
                new Bonus(1L, 14d, Date.valueOf(LocalDate.of(2022, 12, 10)), null),
                rs
        );

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}