package com.studyhelper.db.model.Todo;

import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoGetList implements TodoGetListService {

    private Connection connection;
    private final Logger logger;

    public TodoGetList(){
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TodoGetList.class.getName());
    }

    @Override
    public ObservableList<Todo> all() {
        try{
            return allExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Todo> allExecute() throws SQLException {
        ObservableList<Todo> todoObservableList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM todo");
            while (resultSet.next())
                todoObservableList.add(getTodoFromResultSet(resultSet));

            return todoObservableList;
        }
    }

    private Todo getTodoFromResultSet(ResultSet resultSet) throws SQLException {
        Todo todo = new Todo();
        todo.setId(resultSet.getInt("id"));
        todo.setCompleted(resultSet.getInt("completed") == 1);
        todo.setItem(resultSet.getString("item"));
        todo.setCourse_id(resultSet.getInt("course_id"));

        return todo;
    }

    @Override
    public ObservableList<Todo> byCourseId(int courseId) {
        try{
            return byCourseIdExecute(courseId);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Todo> byCourseIdExecute(int courseId)
            throws SQLException {
        ObservableList<Todo> todoObservableList = FXCollections.observableArrayList();

        PreparedStatement getAllTodoByCourseId = connection.prepareStatement(
                "SELECT * FROM todo WHERE course_id= ?"
        );
        getAllTodoByCourseId.setInt(1, courseId);
        ResultSet resultSet = getAllTodoByCourseId.executeQuery();

        while (resultSet.next())
            todoObservableList.add(getTodoFromResultSet(resultSet));

        return todoObservableList;
    }
}
