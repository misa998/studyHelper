package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeServiceImpl implements TimeService{

    private final Logger logger = Logger.getLogger(TimeServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public ObservableList<Time> getAllTime() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }

        ObservableList<Time> timePeriodList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM time");
            while (resultSet.next()) {
                Time time = new Time();
                time.setId(resultSet.getInt("id"));
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setDuration(Duration.parse(resultSet.getString("duration")));
                //time.setDuration(LocalTime.parse(resultSet.getString("duration"), DateTimeFormatter.ofPattern("HH:mm:ss")));

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
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM time WHERE course_id=");
        stringBuilder.append(course_id);
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());

            Time time = new Time();
            time.setId(resultSet.getInt("id"));
            time.setCourse_id(resultSet.getInt("course_id"));
            time.setDuration(Duration.parse(resultSet.getString("duration")));

            return time;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public boolean updateSumOfTimeForCourse(Time timeToAdd) {
        Time oldTimeValue = getTimeByCourse_id(timeToAdd.getCourse_id());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE time SET duration=");
        stringBuilder.append("\"");
        Duration newTimeValue = oldTimeValue.getDuration().plusHours(timeToAdd.getDuration().toHoursPart()).plusMinutes(timeToAdd.getDuration().toMinutesPart());
        stringBuilder.append(newTimeValue);
        stringBuilder.append("\"");
        stringBuilder.append(" WHERE course_id=");
        stringBuilder.append(timeToAdd.getCourse_id());

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return false;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public boolean insertNewTime(int course_id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO time (\"duration\", \"course_id\") VALUES (\"");
        stringBuilder.append(Duration.of(0, ChronoUnit.SECONDS));
        stringBuilder.append("\", ");
        stringBuilder.append(course_id);
        stringBuilder.append(")");

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return false;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteAllTimeByCourseId(int course_id) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM time WHERE course_id=");
        stringBuilder.append(course_id);
        try(Statement statement = connection.createStatement()) {
            statement.executeQuery(stringBuilder.toString());

            return;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }
}
