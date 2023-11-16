package com.coherent.aqa.java.training.api.matveenko.config;

import com.coherent.aqa.java.training.api.matveenko.exceptions.BadConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties {
    private static final InputStream TEST_PROPS = TestProperties.class.getResourceAsStream("/config.properties");
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try {
            PROPERTIES.load(TEST_PROPS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadConfigException("Error loading config file", e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
