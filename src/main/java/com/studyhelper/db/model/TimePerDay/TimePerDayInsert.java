package com.studyhelper.db.model.TimePerDay;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.model.Time.TimeServiceImpl;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePerDayInsert implements TimePerDayInsertService{

    private final Logger logger;
    private Connection connection;

    public TimePerDayInsert() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger =  Logger.getLogger(TimePerDayInsert.class.getName());
    }

    @Override
    public void add(TimePerDay timePerDay) {
        if(timePerDay.getDuration().toMinutes() < 1)
            return;

        addNewTimePerDay(timePerDay);
        new TimeServiceImpl().update().duration(
                new Time(0, timePerDay.getDuration(), timePerDay.getCourse_id())
        );
    }

    private void addNewTimePerDay(TimePerDay timePerDay) {
        try{
            addExecute(timePerDay);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void addExecute(TimePerDay timePerDay) throws SQLException {
        PreparedStatement addNew = connection.prepareStatement(
                "INSERT INTO timePerDay (date, duration, course_id) " +
                        "VALUES (?, ?, ?)");
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
}
