package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    // class Config -> Singleton
    private  final static String CONFIG_PROPERTIES = "config.properties";
    private static Config config;
    private Properties properties;

    // Implement Singleton
    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    private static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    //Reading data from the config file

    public void loadProperties(String fileName) {
        try(InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                System.out.println("File not found" + fileName);
            }
            properties.load(stream);

        } catch (IOException e) {
            System.err.println("Error while reading a file" + fileName);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        String property = getConfig().properties.getProperty(key);
        if (property == null) {
            throw new RuntimeException("Property not found by key: " + key);
        } else {
            return property;
        }
    }
}
