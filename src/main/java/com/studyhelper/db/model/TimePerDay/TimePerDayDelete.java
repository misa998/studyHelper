package com.studyhelper.db.model.TimePerDay;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayDelete implements TimePerDayDeleteService {

    private final Logger logger;
    private Connection connection;

    public TimePerDayDelete() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger =  Logger.getLogger(TimePerDayDelete.class.getName());
    }

    @Override
    public void byCourseId(int courseId) {
        try {
            byCourseIdExecute(courseId);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void byCourseIdExecute(int courseId) throws SQLException {
        PreparedStatement deleteAllByCourseId = connection.prepareStatement(
                "DELETE FROM timePerDay WHERE course_id= ?"
        );
        deleteAllByCourseId.setInt(1, courseId);
        int affectedRows = deleteAllByCourseId.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }
}
