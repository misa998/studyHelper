package com.studyhelper.db.model.Course;

import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseUpdate implements CourseUpdateService{
    private final Logger logger;
    private Connection connection;

    public CourseUpdate() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseUpdate.class.getName());
    }

    public void name(String name, int id) {
        try{
            updateNameExecute(name, id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateNameExecute(String name, int id) throws SQLException {
        PreparedStatement updateCourseName = connection.prepareStatement(
                "UPDATE course SET name= ? WHERE id= ?");
        updateCourseName.setString(1, name);
        updateCourseName.setInt(2, id);

        int affectedRows = updateCourseName.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }

    public void description(String description, int id) {
        try{
            updateDescriptionExecute(description, id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }


    private void updateDescriptionExecute(String description, int id) throws SQLException {
        PreparedStatement updateCourseDesc = connection.prepareStatement(
                "UPDATE course SET description= ? WHERE id= ?");
        updateCourseDesc.setString(1, description);
        updateCourseDesc.setInt(2, id);

        int affectedRows = updateCourseDesc.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public void due(LocalDate newValue, int id) {
        try{
            updateDueExecute(newValue, id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateDueExecute(LocalDate newValue, int id) throws SQLException {
        PreparedStatement updateCourseDesc = connection.prepareStatement(
                "UPDATE course SET due= ? WHERE id= ?");
        updateCourseDesc.setString(1, newValue.toString());
        updateCourseDesc.setInt(2, id);

        int affectedRows = updateCourseDesc.executeUpdate();
        isUpdated(affectedRows);
    }
}
