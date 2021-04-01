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
    public List<TimePerDay> getAll() {
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
                LocalDate localDate = LocalDate.parse(resultSet.getString("date"));
                localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(localDate);
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
    public boolean addTimePerDay(TimePerDay tpd) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO timePerDay (\"date\", \"hours\", \"course_id\") VALUES (\"");
        sb.append(LocalDate.now());
        sb.append("\", \"");
        if(tpd.getHours() != null)
            sb.append(tpd.getHours());
        else {
            logger.log(Level.WARNING, "hours = null");
        }
        sb.append("\", \"");
        sb.append(tpd.getCourse_id());
        sb.append("\")");

        try (Statement statement = connection.createStatement()) {
            statement.execute(sb.toString());
            logger.log(Level.INFO, sb.toString());

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public List<TimePerDay> getByDateAndCourse_id(int course_id, LocalDate localDate) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        List<TimePerDay> tpdList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM timePerDay WHERE course_id=");
        sb.append(course_id);
        sb.append(" AND date=\"");
        sb.append(localDate.toString());
        sb.append("\"");
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sb.toString());
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate lclDt = LocalDate.parse(resultSet.getString("date"));
                lclDt.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(lclDt);
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
}
