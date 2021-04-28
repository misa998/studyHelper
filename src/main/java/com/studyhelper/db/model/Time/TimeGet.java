package com.studyhelper.db.model.Time;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeGet implements TimeGetService{

    private final Logger logger;
    private Connection connection;

    public TimeGet() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeGet.class.getName());
    }

    @Override
    public Time byId(int id) {
        try{
            return byExecute(id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Time byExecute(int id) throws SQLException {
        PreparedStatement getTimeByCourseId = connection.prepareStatement(
                "SELECT * FROM time id= ?"
        );
        getTimeByCourseId.setInt(1, id);
        ResultSet resultSet = getTimeByCourseId.executeQuery();
        return getTimeFromResultSet(resultSet);
    }

    private Time getTimeFromResultSet(ResultSet resultSet) throws SQLException {
        Time time = new Time();
        time.setId(resultSet.getInt("id"));
        time.setCourse_id(resultSet.getInt("course_id"));
        time.setDuration(Duration.parse(resultSet.getString("duration")));

        return time;
    }

    @Override
    public Time byCourseId(int id) {
        try{
            return byCourseIdExecute(id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Time byCourseIdExecute(int course_id) throws SQLException {
        PreparedStatement getTimeByCourseId = connection.prepareStatement(
                "SELECT * FROM time WHERE course_id= ?"
        );
        getTimeByCourseId.setInt(1, course_id);
        ResultSet resultSet = getTimeByCourseId.executeQuery();
        return getTimeFromResultSet(resultSet);
    }
}
