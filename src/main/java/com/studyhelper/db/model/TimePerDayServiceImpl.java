package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayServiceImpl implements TimePerDayService{

    private final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public ObservableList<TimePerDay> getAllTimePerDayInstances() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        ObservableList<TimePerDay> tpdList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM timePerDay");
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate dateOfStudy = LocalDate.parse(resultSet.getString("date"));
                dateOfStudy.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(dateOfStudy);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setDuration(Duration.parse(resultSet.getString("duration")));
                //time.setDuration(LocalTime.parse(resultSet.getString("duration"), DateTimeFormatter.ofPattern("HH:mm:ss")));

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
    public ObservableList<TimePerDay> getByCourse_id(int course_id) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM timePerDay WHERE course_id=");
        stringBuilder.append(course_id);

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate lclDt = LocalDate.parse(resultSet.getString("date"));
                lclDt.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(lclDt);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setDuration(Duration.parse(resultSet.getString("duration")));

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

    @Override
    public ObservableList<TimePerDay> getByDate(LocalDate localDate) {
        return null;
    }

    @Override
    public void addTimePerDay(TimePerDay timePerDay) {
        if(timePerDay.getDuration().toMinutes() < 1)
            return;
        addNewTimePerDay(timePerDay);
        new TimeServiceImpl().updateSumOfTimeForCourse(new Time(0, timePerDay.getDuration(), timePerDay.getCourse_id()));
    }

    private boolean addNewTimePerDay(TimePerDay timePerDay) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO timePerDay (\"date\", \"duration\", \"course_id\") VALUES (\"");
        stringBuilder.append(LocalDate.now());
        stringBuilder.append("\", \"");
        stringBuilder.append(timePerDay.getDuration());
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
    public ObservableList<TimePerDay> getTimeByDateAndCourse_id(int course_id, LocalDate dateOfStudy) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

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
                time.setDuration(Duration.parse(resultSet.getString("duration")));

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

    @Override
    public void updateDurationForTimePerDay(TimePerDay timePerDay) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE timePerDay SET duration=");
        stringBuilder.append("\"");
        stringBuilder.append(timePerDay.getDuration());
        stringBuilder.append("\"");
        stringBuilder.append(" WHERE id=");
        stringBuilder.append(timePerDay.getId());

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return ;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteAllTimePerDayByCourseId(int id) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM timePerDay WHERE course_id=");
        stringBuilder.append(id);
        logger.log(Level.INFO, stringBuilder.toString());

        try(Statement statement = connection.createStatement()) {
            statement.executeQuery(stringBuilder.toString());

        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }
}
