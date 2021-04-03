package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.source.DataSource;

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

public class CourseServiceImpl implements CourseService{

    private final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public List<Course> getAllCourses() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        List<Course> courses = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSE");

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                course.setDescription(resultSet.getString("description"));
                LocalDate localDate = LocalDate.parse(resultSet.getString("due"));
                localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                course.setDue(localDate);

                courses.add(course);
            }
            return courses;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void insertCourse(Course course) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO course (\"name\", \"description\", \"due\") VALUES (\"");
        sb.append(course.getName());
        sb.append("\", \"");
        sb.append(course.getDescription());
        sb.append("\", \"");
        sb.append(course.getDue().toString());
        sb.append("\")");
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sb.toString());
            DataSource.getInstance().closeConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public Course getCourseByName(String courseName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM course WHERE \"name\"= ");
        sb.append("\"");
        sb.append(courseName);
        sb.append("\"");
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sb.toString());
            Course course = new Course();
            course.setId(resultSet.getInt("id"));
            course.setName(resultSet.getString("name"));
            course.setDescription(resultSet.getString("description"));
            LocalDate localDate = LocalDate.parse(resultSet.getString("due"));
            localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            course.setDue(localDate);

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
    public Course getDueByName(String courseName) {
        return null;
    }


}
