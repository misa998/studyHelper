package com.studyhelper.db.model.TimePerDay;

import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayGetList implements TimePerDayGetListService{

    private final Logger logger;
    private Connection connection;

    public TimePerDayGetList() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger =  Logger.getLogger(TimePerDayGetList.class.getName());
    }

    @Override
    public ObservableList<TimePerDay> all() {
        try{
            return allExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> allExecute() throws SQLException {
        ObservableList<TimePerDay> tpdList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM timePerDay");

            while (resultSet.next())
                tpdList.add(getTimePerDayFromResultSet(resultSet));

            return tpdList;
        }
    }

    @Override
    public ObservableList<TimePerDay> byCourseId(int courseId) {
        try{
            return byCourseIdExecute(courseId);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> byCourseIdExecute(int courseId) throws SQLException {
        ObservableList<TimePerDay> timePerDaysList = FXCollections.observableArrayList();

        PreparedStatement getAllByCourseId = connection.prepareStatement(
                "SELECT * FROM timePerDay WHERE course_id= ? " +
                        "AND date BETWEEN ? AND ?");
        getAllByCourseId.setInt(1, courseId);
        getAllByCourseId.setString(2, LocalDate.now().minusDays(31).toString());
        getAllByCourseId.setString(3, LocalDate.now().toString());
        ResultSet resultSet = getAllByCourseId.executeQuery();

        while (resultSet.next())
            timePerDaysList.add(getTimePerDayFromResultSet(resultSet));

        return timePerDaysList;
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
    public ObservableList<TimePerDay> byDate(LocalDate localDate) {
        try{
            return byDateExecute(localDate);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> byDateExecute(LocalDate localDate) throws SQLException {
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
    public ObservableList<TimePerDay> byCourseIdAndDate
            (int course_id, LocalDate dateOfStudy) {
        try{
            return byCourseIdAndDateExecute(course_id, dateOfStudy);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<TimePerDay> byCourseIdAndDateExecute
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
    public ArrayList<TimePerDay> byCourseIdAndNumberOfDays(int courseId, int days) {
        try{
            return byCourseIdAndNoOfDaysExecute(courseId, days);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return new ArrayList<>();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ArrayList<TimePerDay> byCourseIdAndNoOfDaysExecute(int courseId, int days) throws SQLException {
        ArrayList<TimePerDay> timePerDaysList = new ArrayList<>();

        PreparedStatement getTimeByDateAndCourseId = connection.prepareStatement(
                "SELECT * FROM timePerDay WHERE course_id= ? " +
                        "AND date BETWEEN ? AND ?");
        getTimeByDateAndCourseId.setInt(1, courseId);
        getTimeByDateAndCourseId.setString(2, LocalDate.now().minusDays(days).toString());
        getTimeByDateAndCourseId.setString(3, LocalDate.now().toString());
        ResultSet resultSet = getTimeByDateAndCourseId.executeQuery();

        while (resultSet.next())
            timePerDaysList.add(getTimePerDayFromResultSet(resultSet));

        return timePerDaysList;
    }
}
