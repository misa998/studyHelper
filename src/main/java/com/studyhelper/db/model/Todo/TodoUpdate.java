package com.studyhelper.db.model.Todo;

import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoUpdate implements TodoUpdateService{

    private Connection connection;
    private final Logger logger;

    public TodoUpdate(){
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TodoUpdate.class.getName());
    }

    @Override
    public void byId(Todo todo) {
        try{
            byIdExecute(todo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void byIdExecute(Todo todo) throws SQLException {
        PreparedStatement updateTodo = connection.prepareStatement(
                "UPDATE todo SET completed= ?, item= ? WHERE id= ?");
        updateTodo.setInt(1, todo.getCompletedProperty().get() ? 1 : 0);
        updateTodo.setString(2, todo.getItem());
        updateTodo.setInt(3, todo.getId());

        int affectedRows = updateTodo.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }
}
