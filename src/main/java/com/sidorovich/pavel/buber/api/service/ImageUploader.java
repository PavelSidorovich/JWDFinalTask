package com.sidorovich.pavel.buber.api.service;

import javax.servlet.http.Part;
import java.io.File;

public interface ImageUploader {

    String upload(Part photo, String uploadPath);

    default boolean createDirectoryIfNotExists(String uploadPath) {
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            return uploadDir.mkdir();
        }

        return false;
    }

}
