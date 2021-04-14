package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodoServiceImpl implements TodoService {

    private Connection connection = null;
    private final Logger logger = Logger.getLogger(TodoServiceImpl.class.getName());

    @Override
    public ObservableList<Todo> getAllTodo(){
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        ObservableList<Todo> todoObservableList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM todo");

            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setCompleted(resultSet.getInt("completed") == 1);
                todo.setItem(resultSet.getString("item"));
                todo.setCourse_id(resultSet.getInt("course_id"));

                todoObservableList.add(todo);
            }
            return todoObservableList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public boolean insertTodo(Todo todo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO todo (\"completed\", \"item\", \"course_id\") VALUES (\"");
        int completed = todo.getCompletedProperty().get() ? 1 : 0;
        stringBuilder.append(completed);
        stringBuilder.append("\", \"");
        stringBuilder.append(todo.getItem());
        stringBuilder.append("\", \"");
        stringBuilder.append(todo.getCourse_id());
        stringBuilder.append("\")");

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return false;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            DataSource.getInstance().closeConnection();

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    @Override
    public ObservableList<Todo> getAllTodoByCourseId(int course_id){
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        ObservableList<Todo> todoObservableList = FXCollections.observableArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM todo WHERE course_id=");
        stringBuilder.append(course_id);

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());

            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setCompleted(resultSet.getInt("completed") == 1);
                todo.setItem(resultSet.getString("item"));
                todo.setCourse_id(resultSet.getInt("course_id"));

                todoObservableList.add(todo);
            }
            return todoObservableList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public boolean updateTodo(Todo todo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE todo SET completed=");
        int completed = todo.getCompletedProperty().get() ? 1 : 0;
        stringBuilder.append(completed);
        stringBuilder.append(", item=\"");
        stringBuilder.append(todo.getItem());
        stringBuilder.append("\"");
        stringBuilder.append(" WHERE id=");
        stringBuilder.append(todo.getId());

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return false;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteTodo(Todo todo) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM todo WHERE id=");
        stringBuilder.append(todo.getId());

        try(Statement statement = connection.createStatement()) {
            statement.executeQuery(stringBuilder.toString());

        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

}
