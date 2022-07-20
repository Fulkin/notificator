package com.notificator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for getting properties
 */
public class PropertiesUtil {
    private static final Properties properties;

    private PropertiesUtil() {
    }

    static {
        try (InputStream input = new FileInputStream("/usr/local/tomcat/webapps/notificator/WEB-INF/classes/properties/connection.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("The file does not exist", e);
        }
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
}
