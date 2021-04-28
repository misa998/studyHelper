package com.studyhelper.db.model.Time;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeUpdate implements TimeUpdateService{

    private final Logger logger;
    private Connection connection;

    public TimeUpdate() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TimeUpdate.class.getName());
    }

    @Override
    public void duration(Time timeToAdd) {
        Time oldTimeValue = getOldTimeValue(timeToAdd);

        connection = DataSource.getInstance().openConnection();
        try{
            durationExecution(timeToAdd, oldTimeValue);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Time getOldTimeValue(Time timeToAdd){
        return new TimeServiceImpl().get().byCourseId(timeToAdd.getCourse_id());
    }

    private void durationExecution(Time timeToAdd, Time oldTimeValue)
            throws SQLException {
        PreparedStatement updateTimeForCourse = connection.prepareStatement(
                "UPDATE time SET duration= ? WHERE course_id= ?"
        );
        java.time.Duration newTimeValue = oldTimeValue.getDuration()
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
}
