package com.sidorovich.pavel.buber.core.service;

import com.sidorovich.pavel.buber.api.service.ImageUploader;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class ImageUploaderImpl implements ImageUploader {

    private static final String EMPTY_STRING = "";

    private ImageUploaderImpl() {
    }

    private static class Holder {
        private static final ImageUploaderImpl INSTANCE = new ImageUploaderImpl();
    }

    public static ImageUploaderImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String upload(Part image, String uploadPath) {
        try {
            createDirectoryIfNotExists(uploadPath);
            image.write(uploadPath
                        + File.separator
                        + image.getSubmittedFileName());

            return image.getSubmittedFileName();
        } catch (IOException e) {
            return EMPTY_STRING;
        }
    }

}
