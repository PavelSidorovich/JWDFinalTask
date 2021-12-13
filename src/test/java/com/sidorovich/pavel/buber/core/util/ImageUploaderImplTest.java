package com.sidorovich.pavel.buber.core.util;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ImageUploaderImplTest {

    @Mock
    private Part part;

    private final ImageUploaderImpl imageUploader = ImageUploaderImpl.getInstance();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void upload_shouldReturnFilename_whenEverythingIsFine() {
        try {
            when(part.getSubmittedFileName()).thenReturn("file.jpg");

            assertEquals(imageUploader.upload(part, "images/"), "file.jpg");

            verify(part).write("images/" + File.separator + "file.jpg");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void upload_shouldReturnEmptyString_whenWritingHasError() {
        try {
            when(part.getSubmittedFileName()).thenReturn("file.jpg");
            doThrow(new IOException()).when(part).write("images/" + File.separator + "file.jpg");

            assertEquals(imageUploader.upload(part, "images/"), "");
        } catch (IOException e) {
            fail();
        }
    }

}