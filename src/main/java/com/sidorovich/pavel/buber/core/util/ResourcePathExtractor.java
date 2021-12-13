package com.sidorovich.pavel.buber.core.util;

import com.sidorovich.pavel.buber.api.exception.ResourceFileNotFoundException;
import com.sidorovich.pavel.buber.api.util.Extractor;

import java.net.URL;

public class ResourcePathExtractor implements Extractor<String, String> {

    private static final String CANNOT_FIND_PROPERTY_FILE_MSG = "Cannot find property file: %s";

    private ResourcePathExtractor(){
    }

    @Override
    public String extract(String filename) {
        URL url = getClass().getClassLoader().getResource(filename);

        if (url != null) {
            return url.getPath();
        } else {
            throw new ResourceFileNotFoundException(String.format(CANNOT_FIND_PROPERTY_FILE_MSG, filename));
        }
    }

    public static ResourcePathExtractor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ResourcePathExtractor INSTANCE = new ResourcePathExtractor();
    }

}
