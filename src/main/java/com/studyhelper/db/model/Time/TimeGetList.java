package com.studyhelper.db.model.Time;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeGetList implements TimeGetListService{

    private final Logger logger;
    private Connection connection;

    public TimeGetList() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeGetList.class.getName());
    }

    @Override
    public ObservableList<Time> all() {
        try{
            return allTimeExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Time> allTimeExecute() throws SQLException {
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
}
