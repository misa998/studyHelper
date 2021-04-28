package com.studyhelper.db.model.Time;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeInsert implements TimeInsertService{

    private final Logger logger;
    private Connection connection;

    public TimeInsert() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeInsert.class.getName());
    }

    @Override
    public int add(int course_id) {
        try{
            return addExecute(course_id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return -1;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private int addExecute(int course_id) throws SQLException {
        PreparedStatement insertNewTime = connection.prepareStatement(
                "INSERT INTO time (duration, course_id) VALUES (?, ?)"
        );
        insertNewTime.setString(1, String.valueOf(Duration.ZERO));
        insertNewTime.setInt(2, course_id);

        int affectedRow = insertNewTime.executeUpdate();
        isUpdated(affectedRow);

        return anIdOfNewTime(insertNewTime);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }

    private int anIdOfNewTime(PreparedStatement insertTime) throws SQLException {
        ResultSet generatedKeys = insertTime.getGeneratedKeys();
        if(generatedKeys.next())
            return generatedKeys.getInt(1);
        else
            throw new SQLException("Error");
    }
}
