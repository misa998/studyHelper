package com.studyhelper.db.model.Course;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseGet implements CourseGetService {

    private final Logger logger;
    private Connection connection;

    public CourseGet() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseGet.class.getName());
    }

    @Override
    public Course byId(int id) {
        try{
            return byIdExecute(id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, "Course with that id does not exist. Returning null.");
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Course byIdExecute(int id) throws SQLException {
        PreparedStatement getCourseById = connection.prepareStatement(
                "SELECT * FROM course WHERE id= ?");
        getCourseById.setInt(1, id);
        ResultSet resultSet = getCourseById.executeQuery();

        return getCourseFromResultSet(resultSet);
    }

    @Override
    public Course byName(String name) {
        try{
            return byNameExecute(name);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Course byNameExecute(String name) throws SQLException {
        PreparedStatement getCourseByName = connection.prepareStatement(
                "SELECT * FROM course WHERE name= ?");
        getCourseByName.setString(1, name);
        ResultSet resultSet = getCourseByName.executeQuery();

        return getCourseFromResultSet(resultSet);
    }

    private Course getCourseFromResultSet(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("id"));
        course.setName(resultSet.getString("name"));
        course.setDescription(resultSet.getString("description"));
        LocalDate dueDate = LocalDate.parse(resultSet.getString("due"));
        dueDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        course.setDue(dueDate);

        return course;
    }
}
