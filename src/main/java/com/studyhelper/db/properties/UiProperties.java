package com.studyhelper.db.properties;

import java.io.*;

import java.net.URL;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UiProperties {
    private final Logger logger = Logger.getLogger(UiProperties.class.getName());

    private final String UI_PROPERTIES_PATH = "properties/";
    private final String UI_PROPERTIES_NAME = "ui.properties";

    private Properties properties;

    public UiProperties() {
        try {
            loadFile();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
    public void loadFile() throws IOException {
        try (InputStream input = UiProperties.class
                .getClassLoader().getResourceAsStream(UI_PROPERTIES_PATH + UI_PROPERTIES_NAME)) {

            getProperties(input);

        } catch (NullPointerException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }
    }

    private void getProperties(InputStream in) throws IOException {
        properties = new Properties();
        properties.load(in);
    }

    public URL getResourceURL(String name) {
        return toResourceURL(get(name));
    }

    private URL toResourceURL(String path) {
        return getClass().getResource(path);
    }

    public String get(String name) {
        return properties.getProperty(name);
    }
}