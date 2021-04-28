package com.studyhelper.db.model.TimePerDay;

import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayUpdate implements TimePerDayUpdateService{

    private final Logger logger;
    private Connection connection;

    public TimePerDayUpdate() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger =  Logger.getLogger(TimePerDayUpdate.class.getName());
    }

    @Override
    public void duration(TimePerDay timePerDay) {
        try{
            durationExecute(timePerDay);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void durationExecute(TimePerDay timePerDay)
            throws SQLException {
        PreparedStatement updateDuration = connection.prepareStatement(
                "UPDATE timePerDay SET duration= ? WHERE id= ?");
        updateDuration.setString(1, timePerDay.getDuration().toString());
        updateDuration.setInt(2, timePerDay.getId());

        int affectedRows = updateDuration.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }
}
