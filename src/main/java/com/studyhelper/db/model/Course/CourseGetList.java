package com.studyhelper.db.model.Course;

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

public class CourseGetList implements CourseGetListService {

    private final Logger logger;
    private Connection connection;

    public CourseGetList() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseGetList.class.getName());
    }

    @Override
    public ObservableList<Course> all(){
        try{
            return getAllExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Course> getAllExecute() throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSE");

            while (resultSet.next())
                courses.add(getCourseFromResultSet(resultSet));

            return courses;
        }
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
