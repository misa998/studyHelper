package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeServiceImpl implements TimeService{

    private final Logger logger;
    private Connection connection;

    public TimeServiceImpl() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeServiceImpl.class.getName());
    }

    @Override
    public ObservableList<Time> getAllTime() {
        try{
            return getAllTimeExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Time> getAllTimeExecute() throws SQLException {
        ObservableList<Time> timePeriodList = FXCollections.observableArrayList();
        PreparedStatement getAllTime = connection.prepareStatement(
                "SELECT * FROM time");
        ResultSet resultSet = getAllTime.executeQuery();
        while (resultSet.next())
            timePeriodList.add(getTimeFromResultSet(resultSet));

        return timePeriodList;
    }

    private Time getTimeFromResultSet(ResultSet resultSet) throws SQLException {
        Time time = new Time();
        time.setId(resultSet.getInt("id"));
        time.setCourse_id(resultSet.getInt("course_id"));
        time.setDuration(Duration.parse(resultSet.getString("duration")));

        return time;
    }

    @Override
    public Time getTimeByCourse_id(int course_id) {
        try{
            return getTimeByCourse_idExecute(course_id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Time getTimeByCourse_idExecute(int course_id) throws SQLException {
        PreparedStatement getTimeByCourseId = connection.prepareStatement(
                "SELECT * FROM time WHERE course_id= ?"
        );
        getTimeByCourseId.setInt(1, course_id);
        ResultSet resultSet = getTimeByCourseId.executeQuery();
        return getTimeFromResultSet(resultSet);
    }

    @Override
    public void updateSumOfTimeForCourse(Time timeToAdd) {
        Time oldTimeValue = getOldTimeValue(timeToAdd);

        connection = DataSource.getInstance().openConnection();
        try{
            updateSumOfTimeForCourseExecute(timeToAdd, oldTimeValue);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Time getOldTimeValue(Time timeToAdd){
        return getTimeByCourse_id(timeToAdd.getCourse_id());
    }

    private void updateSumOfTimeForCourseExecute(Time timeToAdd, Time oldTimeValue)
            throws SQLException {
        PreparedStatement updateTimeForCourse = connection.prepareStatement(
                "UPDATE time SET duration= ? WHERE course_id= ?"
        );
        Duration newTimeValue = oldTimeValue.getDuration()
                .plusMinutes(timeToAdd.getDuration().toMinutes());
        updateTimeForCourse.setString(1, newTimeValue.toString());
        updateTimeForCourse.setInt(2, timeToAdd.getCourse_id());

        int affectedRows = updateTimeForCourse.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }

    @Override
    public void insertNewTime(int course_id) {
        try{
            insertNewTimeExecute(course_id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void insertNewTimeExecute(int course_id) throws SQLException {
        PreparedStatement insertNewTime = connection.prepareStatement(
                "INSERT INTO time (duration, course_id) VALUES (?, ?)"
        );
        insertNewTime.setString(1, String.valueOf(Duration.ZERO));
        insertNewTime.setInt(2, course_id);

        int affectedRow = insertNewTime.executeUpdate();
        isUpdated(affectedRow);
    }

    @Override
    public void deleteAllTimeByCourseId(int course_id) {
        try {
            deleteAllTimeByCourseIdExecute(course_id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteAllTimeByCourseIdExecute(int course_id) throws SQLException {
        PreparedStatement deleteTimeById = connection.prepareStatement(
                "DELETE FROM time WHERE course_id= ?"
        );
        deleteTimeById.setInt(1, course_id);
        int affectedRows = deleteTimeById.executeUpdate();
        isUpdated(affectedRows);
    }
}
