package com.studyhelper.db.model.Course;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.model.Time.TimeServiceImpl;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseInsert implements CourseInsertService{
    private final Logger logger;
    private Connection connection;

    public CourseInsert() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseInsert.class.getName());
    }

    @Override
    public void add(Course course) {
        try{
            Course courseNew = insertNewCourse(course);
            new TimeServiceImpl().insert().add(courseNew.getId());
        } catch (NullPointerException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private Course insertNewCourse(Course course) {
        Course course1 = new CourseServiceImpl().get().byName(course.getName());
        if(course1 != null)
            return course1;

        try{
            insertNewCourseExecute(course);
            return new CourseServiceImpl().get().byName(course.getName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void insertNewCourseExecute(Course course) throws SQLException {
        PreparedStatement insertNewCourse = connection.prepareStatement(
                "INSERT INTO course (name, description, due) VALUES (?, ?, ?)");
        insertNewCourse.setString(1, course.getName());
        insertNewCourse.setString(2, course.getDescription());
        insertNewCourse.setString(3, course.getDue().toString());

        int affectedRows = insertNewCourse.executeUpdate();
        isUpdated(affectedRows);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }
}
