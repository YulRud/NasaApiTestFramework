package com.yulrud.nasaapi.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
    private static final String propertyFilePath = "src/main/resources/Configuration.properties";
    private Properties properties;

    public ConfigFileReader() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getApplicationBaseUrl() {
        String baseUrl = properties.getProperty("baseUrl");

        if (baseUrl != null) return baseUrl;
        else throw new RuntimeException("baseUrl not specified in the Configuration.properties file.");
    }

    public String getKey() {
        String key = properties.getProperty("key");

        if (key != null) return key;
        else throw new RuntimeException("key not specified in the Configuration.properties file.");
    }
}
