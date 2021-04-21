package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayServiceImpl implements TimePerDayService{

    private final Logger logger;
    private Connection connection;

    public TimePerDayServiceImpl() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger =  Logger.getLogger(CourseServiceImpl.class.getName());
    }

    @Override
    public ObservableList<TimePerDay> getAllTimePerDayInstances() {
        try{
            return getAllTimePerDayInstancesExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> getAllTimePerDayInstancesExecute() throws SQLException {
        ObservableList<TimePerDay> tpdList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM timePerDay");

            while (resultSet.next())
                tpdList.add(getTimePerDayFromResultSet(resultSet));

            return tpdList;
        }
    }

    private TimePerDay getTimePerDayFromResultSet(ResultSet resultSet) throws SQLException {
        TimePerDay timePerDay = new TimePerDay();
        timePerDay.setId(resultSet.getInt("id"));
        LocalDate dateOfStudy = LocalDate.parse(resultSet.getString("date"));
        dateOfStudy.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        timePerDay.setDate(dateOfStudy);
        timePerDay.setCourse_id(resultSet.getInt("course_id"));
        timePerDay.setDuration(Duration.parse(resultSet.getString("duration")));

        return timePerDay;
    }

    @Override
    public ObservableList<TimePerDay> getByCourse_id(int course_id) {
        try{
            return getByCourse_idExecute(course_id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> getByCourse_idExecute(int course_id) throws SQLException {
        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

        PreparedStatement getAllByCourseId = connection.prepareStatement(
                "SELECT * FROM timePerDay WHERE course_id= ?");
        getAllByCourseId.setInt(1, course_id);
        ResultSet resultSet = getAllByCourseId.executeQuery();

        while (resultSet.next())
            timePerDaysList.add(getTimePerDayFromResultSet(resultSet));

        return timePerDaysList;
    }

    @Override
    public ObservableList<TimePerDay> getByDate(LocalDate localDate) {
        try{
            return getByDateExecute(localDate);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> getByDateExecute(LocalDate localDate) throws SQLException {
        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

        PreparedStatement getTPDByDate = connection.prepareStatement(
                "SELECT * FROM timePerDay WHERE date= ?");
        getTPDByDate.setString(1, localDate.toString());
        ResultSet resultSet = getTPDByDate.executeQuery();

        while (resultSet.next())
            timePerDaysList.add(getTimePerDayFromResultSet(resultSet));

        return timePerDaysList;
    }

    @Override
    public void addTimePerDay(TimePerDay timePerDay) {
        if(timePerDay.getDuration().toMinutes() < 1)
            return;
        new TimePerDayServiceImpl().addNewTimePerDay(timePerDay);
        new TimeServiceImpl().updateSumOfTimeForCourse(
                new Time(0, timePerDay.getDuration(), timePerDay.getCourse_id())
        );
    }

    private void addNewTimePerDay(TimePerDay timePerDay) {
        try{
            addNewTimePerDayExecute(timePerDay);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void addNewTimePerDayExecute(TimePerDay timePerDay) throws SQLException {
        PreparedStatement addNew = connection.prepareStatement(
                "INSERT INTO timePerDay (date, duration, course_id) VALUES " +
                        "(?, ?, ?)");
        addNew.setString(1, timePerDay.getDate().toString());
        addNew.setString(2, timePerDay.getDuration().toString());
        addNew.setInt(3, timePerDay.getCourse_id());

        int affectedRows = addNew.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }

    @Override
    public ObservableList<TimePerDay> getTimeByDateAndCourse_id
            (int course_id, LocalDate dateOfStudy) {
        try{
            return getTimeByDateAndCourseIdExecute(course_id, dateOfStudy);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> getTimeByDateAndCourseIdExecute
            (int course_id, LocalDate dateOfStudy) throws SQLException {
        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

        PreparedStatement getTimeByDateAndCourseId = connection.prepareStatement(
                "SELECT * FROM timePerDay WHERE course_id= ? AND date= ?");
        getTimeByDateAndCourseId.setInt(1, course_id);
        getTimeByDateAndCourseId.setString(1, dateOfStudy.toString());
        ResultSet resultSet = getTimeByDateAndCourseId.executeQuery();

        while (resultSet.next())
            timePerDaysList.add(getTimePerDayFromResultSet(resultSet));

        return timePerDaysList;
    }

    @Override
    public void updateDurationForTimePerDay(TimePerDay timePerDay) {
        try{

            updateDurationForTimePerDayExecute(timePerDay);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateDurationForTimePerDayExecute(TimePerDay timePerDay)
            throws SQLException {
        PreparedStatement updateDuration = connection.prepareStatement(
                "UPDATE timePerDay SET duration= ? WHERE id= ?");
        updateDuration.setString(1, timePerDay.getDuration().toString());
        updateDuration.setInt(2, timePerDay.getId());

        int affectedRows = updateDuration.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public void deleteAllTimePerDayByCourseId(int id) {
        try {
            deleteAllTimePerDayByCourseIdExecute(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteAllTimePerDayByCourseIdExecute(int id) throws SQLException {
        PreparedStatement deleteAllByCourseId = connection.prepareStatement(
                "DELETE FROM timePerDay WHERE course_id= ?"
        );
        deleteAllByCourseId.setInt(1, id);
        int affectedRows = deleteAllByCourseId.executeUpdate();
        isUpdated(affectedRows);
    }
}
