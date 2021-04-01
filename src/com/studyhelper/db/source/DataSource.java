package com.studyhelper.db.source;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.TimePerDay;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private static Logger logger = Logger.getLogger(DataSource.class.getName());

    private static DataSource instance = new DataSource();
    private DataSource(){}
    public static DataSource getInstance(){
        return instance;
    }

    public static final String DB_NAME = "studymng.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Java\\projects\\StudyHelperDB\\" + DB_NAME;

    private Connection connection;

    public Connection openConnection(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
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
