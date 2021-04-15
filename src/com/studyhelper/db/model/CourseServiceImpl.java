package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseServiceImpl implements CourseService{

    private final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public ObservableList<Course> getAllCourses() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        ObservableList<Course> courseArrayList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSE");

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                course.setDescription(resultSet.getString("description"));
                LocalDate dueDate = LocalDate.parse(resultSet.getString("due"));
                dueDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                course.setDue(dueDate);

                courseArrayList.add(course);
            }
            return courseArrayList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void updateCourseName(String name, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE course SET name=\"");
        stringBuilder.append(name);
        stringBuilder.append("\" ");
        stringBuilder.append("WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void insertCourse(Course course) {
        insertNewCourse(course);
        Course courseNew = getCourseByName(course.getName());
        new TimeServiceImpl().insertNewTime(courseNew.getId());
    }

    private void insertNewCourse(Course course) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO course (\"name\", \"description\", \"due\") VALUES (\"");
        stringBuilder.append(course.getName());
        stringBuilder.append("\", \"");
        stringBuilder.append(course.getDescription());
        stringBuilder.append("\", \"");
        stringBuilder.append(course.getDue().toString());
        stringBuilder.append("\")");

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }


    @Override
    public Course getCourseByName(String courseName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM course WHERE \"name\"= ");
        stringBuilder.append("\"");
        stringBuilder.append(courseName);
        stringBuilder.append("\"");
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            Course course = new Course();
            course.setId(resultSet.getInt("id"));
            course.setName(resultSet.getString("name"));
            course.setDescription(resultSet.getString("description"));
            LocalDate dueDate = LocalDate.parse(resultSet.getString("due"));
            dueDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            course.setDue(dueDate);

            return course;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteCourseById(int id) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM course WHERE id=");
        stringBuilder.append(id);

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

    @Override
    public Course getCourseById(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM course WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            Course course = new Course();
            course.setId(resultSet.getInt("id"));
            course.setName(resultSet.getString("name"));
            course.setDescription(resultSet.getString("description"));
            LocalDate dueDate = LocalDate.parse(resultSet.getString("due"));
            dueDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            course.setDue(dueDate);

            return course;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void updateCourseDescription(String description, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE course SET description=\"");
        stringBuilder.append(description);
        stringBuilder.append("\" ");
        stringBuilder.append("WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteAllDataAboutCourse(int id) {
        deleteCourseById(id);
        new TodoServiceImpl().deleteAllTodoByCourseId(id);
        new TimePerDayServiceImpl().deleteAllTimePerDayByCourseId(id);
        new TimeServiceImpl().deleteAllTimeByCourseId(id);
    }
}