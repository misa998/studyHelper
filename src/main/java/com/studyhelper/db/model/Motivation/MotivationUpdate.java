package com.studyhelper.db.model.Motivation;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationUpdate implements MotivationUpdateService{

    private final Logger logger;
    private Connection connection;

    public MotivationUpdate() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationUpdate.class.getName());
    }

    @Override
    public void title(String title, int id) {
        try {
            titleExecute(title, id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void titleExecute(String title, int id) throws SQLException {
        PreparedStatement updateMotivationTitle = connection.prepareStatement(
                "UPDATE motivation SET title= ? WHERE id= ?"
        );
        updateMotivationTitle.setString(1, title);
        updateMotivationTitle.setInt(2, id);
        int affectedRecords = updateMotivationTitle.executeUpdate();

        isUpdated(affectedRecords);
    }

    @Override
    public void body(String body, int id) {
        try {
            bodyExecute(body, id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void bodyExecute(String body, int id) throws SQLException {
        PreparedStatement updateMotivationBody = connection.prepareStatement(
                "UPDATE motivation SET body= ? WHERE id= ?"
        );
        updateMotivationBody.setString(1, body);
        updateMotivationBody.setInt(2, id);
        int affectedRecords = updateMotivationBody.executeUpdate();

        isUpdated(affectedRecords);
    }


    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }
}
