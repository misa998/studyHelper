package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeServiceImpl implements TimeService{

    private final Logger logger = Logger.getLogger(TimeServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public List<Time> getAllTime() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        List<Time> timePeriodList = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM time");
            while (resultSet.next()) {
                Time time = new Time();
                time.setId(resultSet.getInt("id"));
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setPeriod(LocalTime.parse(resultSet.getString("period"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                timePeriodList.add(time);
            }
            return timePeriodList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public Time getTimeByCourse_id(int course_id) {
        return null;
    }

    @Override
    public boolean updateTime(Time time) {
        return false;
    }

    @Override
    public boolean addTimeInstance(Time time) {
        return false;
    }
}
