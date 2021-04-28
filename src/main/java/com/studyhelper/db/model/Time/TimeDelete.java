package com.studyhelper.db.model.Time;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeDelete implements TimeDeleteService{

    private final Logger logger;
    private Connection connection;

    public TimeDelete() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeDelete.class.getName());
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

    private void byCourseIdExecute(int course_id) throws SQLException {
        PreparedStatement deleteTimeById = connection.prepareStatement(
                "DELETE FROM time WHERE course_id= ?"
        );
        deleteTimeById.setInt(1, course_id);
        int affectedRows = deleteTimeById.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }
}
