package com.studyhelper.db.model.Motivation;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationDelete implements MotivationDeleteService {

    private final Logger logger;
    private Connection connection;

    public MotivationDelete() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationDelete.class.getName());
    }

    @Override
    public void byId(int id) {
        try {
            byIdExecute(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void byIdExecute(int id) throws SQLException {
        PreparedStatement deleteMotivationById = connection.prepareStatement(
                "DELETE FROM motivation WHERE id= ?"
        );
        deleteMotivationById.setInt(1, id);
        int affectedRows = deleteMotivationById.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }
}
