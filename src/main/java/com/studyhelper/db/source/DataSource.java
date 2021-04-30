package com.studyhelper.db.source;

import com.studyhelper.db.properties.DatabaseProperties;

import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());

    private static DataSource instance = new DataSource();
    private DataSource(){}
    public static DataSource getInstance(){
        return instance;
    }

    public final String DB_FILE_NAME = new DatabaseProperties().get("DB_FILE_NAME");
    public final String URL = new DatabaseProperties().get("URL") + DB_FILE_NAME;

    private Connection connection;

    public void firstConnection(){
        new CreateNewSchema().createDatabaseSchema();
    }

    public Connection openConnection(){
        try{
            connection = DriverManager.getConnection(URL);
            return connection;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
            return null;
        }
    }

    public void closeConnection(){
        try{
            connection.close();
        } catch (SQLException | NullPointerException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
