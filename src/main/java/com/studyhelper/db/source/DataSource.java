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
/*
    public static final String DB_FILE_NAME = "studyHelperDatabase.db";
    public static final String URL = "jdbc:sqlite:" + DB_FILE_NAME;
*/
/*
    public final String DB_FILE_NAME = "studyHelperDatabase.db";
    public final String URL = "jdbc:sqlite::resource:" + DB_FILE_NAME;
*/

    public final String DB_FILE_NAME = new DatabaseProperties().getDB_FILE_NAME();
    public final String URL = new DatabaseProperties().getURL() + DB_FILE_NAME;

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
            return null;
        }
    }

    public boolean closeConnection(){
        try{
            if(connection != null)
                connection.close();

            return true;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
}
