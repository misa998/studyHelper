package com.studyhelper.db.model.Course;

import com.studyhelper.db.model.TimePerDayServiceImpl;
import com.studyhelper.db.model.TimeServiceImpl;
import com.studyhelper.db.model.TodoServiceImpl;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDelete implements CourseDeleteService {
    private final Logger logger;
    private Connection connection;

    public CourseDelete() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseDelete.class.getName());
    }

    @Override
    public void byId(int id) {
        try {
            deleteAllDataAboutCourse(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete course with that id.");
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteByIdExecute(int id) throws SQLException {
        PreparedStatement deleteCourseById = connection.prepareStatement("DELETE FROM course WHERE id= ?");
        deleteCourseById.setInt(1, id);
        int affectedRows = deleteCourseById.executeUpdate();
        isUpdated(affectedRows);
    }

    private void deleteAllDataAboutCourse(int id) throws SQLException {
        deleteByIdExecute(id);
        new TodoServiceImpl().deleteAllTodoByCourseId(id);
        new TimePerDayServiceImpl().deleteAllTimePerDayByCourseId(id);
        new TimeServiceImpl().deleteAllTimeByCourseId(id);
    }

    private void isUpdated(int affectedRows) throws SQLException {
        if(affectedRows != 1)
            throw new SQLException("Non affected");
    }
}
