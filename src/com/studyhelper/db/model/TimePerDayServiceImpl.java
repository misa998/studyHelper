package com.studyhelper.db.model;

import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayServiceImpl implements TimePerDayService{

    private final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public List<TimePerDay> getAllTimePerDayInstances() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        List<TimePerDay> tpdList = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM timePerDay");
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate dateOfStudy = LocalDate.parse(resultSet.getString("date"));
                dateOfStudy.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(dateOfStudy);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setHours(LocalTime.parse(resultSet.getString("hours"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                tpdList.add(time);
            }
            return tpdList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public TimePerDay getByCourse_id(int course_id) {
        return null;
    }

    @Override
    public List<TimePerDay> getByDate(LocalDate localDate) {
        return null;
    }

    @Override
    public boolean addTimePerDay(TimePerDay timePerDay) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO timePerDay (\"date\", \"hours\", \"course_id\") VALUES (\"");
        stringBuilder.append(LocalDate.now());
        stringBuilder.append("\", \"");
        if(timePerDay.getHours().isAfter(LocalTime.of(0,0))) {
            System.out.println(timePerDay.getHours());
            stringBuilder.append(timePerDay.getHours());
        }
        else {
            System.out.println(timePerDay.getHours());
            logger.log(Level.WARNING, "time < one minute");
            return false;
        }
        stringBuilder.append("\", \"");
        stringBuilder.append(timePerDay.getCourse_id());
        stringBuilder.append("\")");

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public List<TimePerDay> getTimeByDateAndCourse_id(int course_id, LocalDate dateOfStudy) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        List<TimePerDay> timePerDaysList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM timePerDay WHERE course_id=");
        stringBuilder.append(course_id);
        stringBuilder.append(" AND date=\"");
        stringBuilder.append(dateOfStudy.toString());
        stringBuilder.append("\"");
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate lclDt = LocalDate.parse(resultSet.getString("date"));
                lclDt.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(lclDt);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setHours(LocalTime.parse(resultSet.getString("hours"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                timePerDaysList.add(time);
            }
            return timePerDaysList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }
}
