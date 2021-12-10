package com.sidorovich.pavel.buber.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyWrapper extends Properties {

    private static final Logger LOG = LogManager.getLogger(PropertyWrapper.class);

    private final Properties properties;

    public PropertyWrapper(String propertyFilePath) throws IOException {
        this.properties = new Properties();
        try {
            properties.load(new FileReader(propertyFilePath));
        } catch (IOException e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

}
