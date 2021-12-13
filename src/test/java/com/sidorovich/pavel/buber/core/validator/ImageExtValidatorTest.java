package com.sidorovich.pavel.buber.core.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

public class ImageExtValidatorTest {

    private final ImageExtValidator imageExtValidator = ImageExtValidator.getInstance();
    private final ResourceBundle rs = ResourceBundle.getBundle("l10n.page.error", Locale.US);

    @Test
    public void validate_shouldReturnErrorMsg_whenFilenameIsInvalid() {
        Map<String, String> actual = imageExtValidator.validate("invalid", rs);
        HashMap<String, String> expected = new HashMap<>();

        expected.put("carPhoto", "Valid image extension is required (*.jpeg, *.jpg or *.png)");

        assertEquals(actual, expected);
    }

    @DataProvider(name = "DataProvider")
    public Object[][] getFilenamesAndResult() {
        return new Object[][] {
                { "file.jpg" },
                {"file.jpeg"},
                {"file.png"},
        };
    }

    @Test(dataProvider = "DataProvider")
    public void validate_shouldReturnEmptyMap_whenFilenameIsValid(String filename) {
        Map<String, String> actual = imageExtValidator.validate(filename, rs);

        assertEquals(actual, Collections.EMPTY_MAP);
    }

}