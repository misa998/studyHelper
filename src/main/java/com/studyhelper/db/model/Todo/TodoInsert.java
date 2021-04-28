package com.studyhelper.db.model.Todo;

import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoInsert implements TodoInsertService{

    private Connection connection;
    private final Logger logger;

    public TodoInsert(){
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TodoInsert.class.getName());
    }

    @Override
    public void add(Todo todo) {
        try{
            addExecute(todo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void addExecute(Todo todo) throws SQLException {
        PreparedStatement insertTodo = connection.prepareStatement(
                "INSERT INTO todo (completed, item, course_id) VALUES (?, ?, ?)"
        );
        insertTodo.setInt(1, todo.getCompletedProperty().get() ? 1 : 0);
        insertTodo.setString(2, todo.getItem());
        insertTodo.setInt(3, todo.getCourse_id());

        int affectedRows = insertTodo.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }
}
