package com.studyhelper.db.model.Motivation;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationInsert implements MotivationInsertService{

    private final Logger logger;
    private Connection connection;

    public MotivationInsert() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationInsert.class.getName());
    }

    @Override
    public int add(Motivation motivation) {
        try {
            return addExecute(motivation);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return -1;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private int addExecute(Motivation motivation) throws SQLException {
        PreparedStatement insertMotivation = connection.prepareStatement(
                "INSERT INTO motivation (title, body) VALUES (?, ?)"
        );
        insertMotivation.setString(1, motivation.getTitle());
        insertMotivation.setString(2, motivation.getBody());
        int affectedRows = insertMotivation.executeUpdate();

        isUpdated(affectedRows);

        return anIdOfNewMotivation(insertMotivation);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }

    private int anIdOfNewMotivation(PreparedStatement insertMotivation) throws SQLException {
        ResultSet generatedKeys = insertMotivation.getGeneratedKeys();
        if(generatedKeys.next())
            return generatedKeys.getInt(1);
        else
            throw new SQLException("Error");
    }
}
