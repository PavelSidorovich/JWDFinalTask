package com.sidorovich.pavel.buber.core.util;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ResourceBundleExtractorImplTest {

    @Mock
    CommandRequest request;

    private final ResourceBundleExtractorImpl bundleExtractor = ResourceBundleExtractorImpl.getInstance();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void extractResourceBundle_shouldReturnValidResourceBundle_whenCookieExists() {
        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie("lang", "ru_RU") });

        ResourceBundle resourceBundle = bundleExtractor.extractResourceBundle(request, "test");

        assertEquals(resourceBundle.getLocale(), new Locale("ru", "RU"));
    }

    @Test
    public void extractResourceBundle_shouldReturnENResourceBundle_whenCookieValueIsInvalid() {
        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie("lang", "wt_RU") });

        ResourceBundle resourceBundle = bundleExtractor.extractResourceBundle(request, "test");

        assertEquals(resourceBundle.getLocale(), new Locale("en", "US"));
    }

}