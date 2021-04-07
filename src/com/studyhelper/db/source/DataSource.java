package com.studyhelper.db.source;

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

    public static final String DB_FILE_NAME = "studymng.db";
    public static final String URL = "jdbc:sqlite:D:\\Java\\projects\\StudyHelperDB\\" + DB_FILE_NAME;

    private Connection connection;

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
