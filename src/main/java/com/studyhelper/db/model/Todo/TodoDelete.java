package com.studyhelper.db.model.Todo;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoDelete implements TodoDeleteService{
    private Connection connection;
    private final Logger logger;

    public TodoDelete(){
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TodoDelete.class.getName());
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
        PreparedStatement deleteTodoByCourseId = connection.prepareStatement(
                "DELETE FROM todo WHERE course_id= ?"
        );
        deleteTodoByCourseId.setInt(1, courseId);

        int affectedRows = deleteTodoByCourseId.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
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
        PreparedStatement deleteTodo = connection.prepareStatement(
                "DELETE FROM todo WHERE id= ?"
        );
        deleteTodo.setInt(1, id);
        int affectedRows = deleteTodo.executeUpdate();
        isUpdated(affectedRows);
    }
}
