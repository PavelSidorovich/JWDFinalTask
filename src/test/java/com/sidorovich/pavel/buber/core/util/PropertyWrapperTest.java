package com.sidorovich.pavel.buber.core.util;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.*;

public class PropertyWrapperTest {

    private PropertyWrapper propertyWrapper;

    @Test
    public void getProperty_shouldReturnPropertyValue_whenResourceExists() {
        try {
            propertyWrapper = new PropertyWrapper("src/test/resources/test.properties");

            assertEquals(propertyWrapper.getProperty("label.page.title"), "My bonuses");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void getProperty_shouldThrowIOException_whenResourceDoesNotExists() {
        try {
            propertyWrapper = new PropertyWrapper("test1");

            fail();
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

}