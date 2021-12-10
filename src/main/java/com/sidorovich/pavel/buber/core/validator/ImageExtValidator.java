package com.sidorovich.pavel.buber.core.validator;

import com.sidorovich.pavel.buber.api.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageExtValidator implements Validator<String, Map<String, String>> {

    private static final String IMAGE_EXTENSION_REGEX = "(.jpeg)|(.jpg)|(.png)\\b";
    private static final String INVALID_FILE_EXTENSION_MSG =
            "Valid image extension is required (*.jpeg, *.jpg or *.png)";
    private static final String CAR_PHOTO_ATTR_PARAM_NAME = "carPhoto";

    private final Pattern pattern;

    private ImageExtValidator() {
        pattern = Pattern.compile(IMAGE_EXTENSION_REGEX);
    }

    public static ImageExtValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<String, String> validate(String carPhotoFilename) {
        final Map<String, String> errorsByMessages = new HashMap<>();
        final Matcher matcher = pattern.matcher(carPhotoFilename);

        if (!matcher.find()) {
            errorsByMessages.put(CAR_PHOTO_ATTR_PARAM_NAME, INVALID_FILE_EXTENSION_MSG);
        }

        return errorsByMessages;
    }

    private static class Holder {
        private static final ImageExtValidator INSTANCE = new ImageExtValidator();
    }

}