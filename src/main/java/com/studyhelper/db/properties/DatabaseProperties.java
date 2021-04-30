package com.studyhelper.db.properties;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseProperties {
    private final Logger logger = Logger.getLogger(DatabaseProperties.class.getName());
    private String DATABASE_PROPERTIES_PATH = "properties/";
    private String DATABASE_PROPERTIES_NAME = "database.properties";

    private Properties properties;

    public DatabaseProperties() {
        try {
            laodFile();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void laodFile() throws IOException {
        try(InputStream in = UiProperties.class.getClassLoader().
                        getResourceAsStream(DATABASE_PROPERTIES_PATH + DATABASE_PROPERTIES_NAME)){
            getProperties(in);
        } catch (NullPointerException | IOException e){
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }
    }

    private void getProperties(InputStream in) throws IOException {
        properties = new Properties();
        properties.load(in);
    }

    public String get(String name){
        return properties.getProperty(name);
    }
}
