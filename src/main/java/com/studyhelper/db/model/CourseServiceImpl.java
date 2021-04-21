package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseServiceImpl implements CourseService{

    private final Logger logger;
    private Connection connection;

    public CourseServiceImpl() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CourseServiceImpl.class.getName());
    }

    @Override
    public ObservableList<Course> getAllCourses() {
        try{
          return getAllCoursesExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Course> getAllCoursesExecute() throws SQLException {
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

    @Override
    public void updateCourseName(String name, int id) {
        try{
            updateCourseNameExecute(name, id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateCourseNameExecute(String name, int id) throws SQLException {
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

    @Override
    public void insertCourse(Course course) {
        try{
            Course courseNew = insertNewCourse(course);
            new TimeServiceImpl().insertNewTime(courseNew.getId());
        } catch (NullPointerException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private Course insertNewCourse(Course course) {
        Course course1 = new CourseServiceImpl().getCourseByName(course.getName());
        if(course1 != null)
            return course1;

        try{
            insertNewCourseExecute(course);
            return new CourseServiceImpl().getCourseByName(course.getName());
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


    @Override
    public Course getCourseByName(String courseName) {
        try{
            return getCourseByNameExecute(courseName);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Course getCourseByNameExecute(String courseName) throws SQLException{
        PreparedStatement getCourseByName = connection.prepareStatement(
                "SELECT * FROM course WHERE name= ?");
        getCourseByName.setString(1, courseName);
        ResultSet resultSet = getCourseByName.executeQuery();

        return getCourseFromResultSet(resultSet);
    }

    @Override
    public void deleteCourseById(int id) {
        try {
            deleteCourseByIdExecute(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete course with that id.");
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteCourseByIdExecute(int id) throws SQLException {
        PreparedStatement deleteCourseById = connection.prepareStatement("DELETE FROM course WHERE id= ?");
        deleteCourseById.setInt(1, id);
        int affectedRows = deleteCourseById.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public Course getCourseById(int id) {
        try{
            return getCourseByIdExecute(id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, "Course with that id does not exist. Returning null.");
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Course getCourseByIdExecute(int id) throws SQLException {
        PreparedStatement getCourseById = connection.prepareStatement(
                "SELECT * FROM course WHERE id= ?");
        getCourseById.setInt(1, id);
        ResultSet resultSet = getCourseById.executeQuery();

        return getCourseFromResultSet(resultSet);
    }

    @Override
    public void updateCourseDescription(String description, int id) {
        try{
            updateCourseDescriptionExecute(description, id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateCourseDescriptionExecute(String description, int id) throws SQLException {
        PreparedStatement updateCourseDesc = connection.prepareStatement(
                "UPDATE course SET description= ? WHERE id= ?");
        updateCourseDesc.setString(1, description);
        updateCourseDesc.setInt(2, id);

        int affectedRows = updateCourseDesc.executeUpdate();
        isUpdated(affectedRows);
    }

    @Override
    public void deleteAllDataAboutCourse(int id) {
        deleteCourseById(id);
        new TodoServiceImpl().deleteAllTodoByCourseId(id);
        new TimePerDayServiceImpl().deleteAllTimePerDayByCourseId(id);
        new TimeServiceImpl().deleteAllTimeByCourseId(id);
    }
}
