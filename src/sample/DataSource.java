package sample;

import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;

public class DataSource {

    private static DataSource instance = new DataSource();
    private DataSource(){}
    public static DataSource getInstance(){
        return instance;
    }

    public static final String DB_NAME = "studymng.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Java\\projects\\StudyHelperDB\\" + DB_NAME;

    private Connection connection;

    public boolean openConnection(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    public boolean closeConnection(){
        try{
            if(connection != null)
                connection.close();

            return true;
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses(){
        List<Course> courses = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSE");
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                course.setDescription(resultSet.getString("description"));
                course.setStudent_id(resultSet.getInt("student_id"));
                LocalDate localDate = LocalDate.parse(resultSet.getString("due"));
                localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                course.setDue(localDate);

                courses.add(course);
            }
            return courses;
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Time> getAllTime(){
        List<Time> periods = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM time");
            while (resultSet.next()) {
                Time time = new Time();
                time.setId(resultSet.getInt("id"));
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setPeriod(LocalTime.parse(resultSet.getString("period"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                periods.add(time);
            }
            return periods;
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
