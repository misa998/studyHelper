package com.studyhelper.db.model;

import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoServiceImpl implements TodoService {

    private Connection connection;
    private final Logger logger;

    public TodoServiceImpl(){
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(TodoServiceImpl.class.getName());
    }

    @Override
    public ObservableList<Todo> getAllTodo(){
        try{
            return getAllTodoExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Todo> getAllTodoExecute() throws SQLException {
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
    public void insertTodo(Todo todo){
        try{

            insertTodoExecute(todo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void insertTodoExecute(Todo todo) throws SQLException {
        PreparedStatement insertTodo = connection.prepareStatement(
                "INSERT INTO todo (completed, item, course_id) VALUES (?, ?, ?)"
        );
        insertTodo.setInt(1, todo.getCompletedProperty().get() ? 1 : 0);
        insertTodo.setString(2, todo.getItem());
        insertTodo.setInt(3, todo.getCourse_id());

        int affectedRows = insertTodo.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public ObservableList<Todo> getAllTodoByCourseId(int course_id){
        try{
            return getAllTodoByCourseIdExecute(course_id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Todo> getAllTodoByCourseIdExecute(int course_id)
            throws SQLException {
        ObservableList<Todo> todoObservableList = FXCollections.observableArrayList();

        PreparedStatement getAllTodoByCourseId = connection.prepareStatement(
                "SELECT * FROM todo WHERE course_id= ?"
        );
        getAllTodoByCourseId.setInt(1, course_id);
        ResultSet resultSet = getAllTodoByCourseId.executeQuery();

        while (resultSet.next())
            todoObservableList.add(getTodoFromResultSet(resultSet));

        return todoObservableList;
    }

    @Override
    public void updateTodo(Todo todo){
        try{

            updateTodoExecute(todo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateTodoExecute(Todo todo) throws SQLException {
        PreparedStatement updateTodo = connection.prepareStatement(
                "UPDATE todo SET completed= ?, item= ? WHERE id= ?");
        updateTodo.setInt(1, todo.getCompletedProperty().get() ? 1 : 0);
        updateTodo.setString(2, todo.getItem());
        updateTodo.setInt(3, todo.getId());

        int affectedRows = updateTodo.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public void deleteTodo(Todo todo) {
        try {

            deleteTodoExecute(todo);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteTodoExecute(Todo todo) throws SQLException {
        PreparedStatement deleteTodo = connection.prepareStatement(
                "DELETE FROM todo WHERE id= ?"
        );
        deleteTodo.setInt(1, todo.getId());
        int affectedRows = deleteTodo.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public void deleteAllTodoByCourseId(int id) {
        try {

            deleteAllTodoByCourseIdExecute(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteAllTodoByCourseIdExecute(int id) throws SQLException {
        PreparedStatement deleteTodoByCourseId = connection.prepareStatement(
                "DELETE FROM todo WHERE course_id= ?"
        );
        deleteTodoByCourseId.setInt(1, id);

        int affectedRows = deleteTodoByCourseId.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }
}
