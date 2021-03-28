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
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private static Logger logger = Logger.getLogger(DataSource.class.getName());

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
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
    public boolean closeConnection(){
        try{
            if(connection != null)
                connection.close();

            return true;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
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
                LocalDate localDate = LocalDate.parse(resultSet.getString("due"));
                localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                course.setDue(localDate);

                courses.add(course);
            }
            return courses;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
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
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public void insertAll(){

    }

    public void insertCourse(Course course) {
        StringBuilder sb = new StringBuilder();
        try (Statement statement = connection.createStatement()) {
            sb.append("INSERT INTO course (\"name\", \"description\", \"due\") VALUES (\"");
            sb.append(course.getName());
            sb.append("\", \"");
            sb.append(course.getDescription());
            sb.append("\", \"");
            sb.append(course.getDue().toString());
            sb.append("\")");
            statement.execute(sb.toString());
            logger.log(Level.INFO, sb.toString());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public Course getCourseByName(String courseName){
        StringBuilder sb = new StringBuilder();
        try(Statement statement = connection.createStatement()) {
            sb.append("SELECT * FROM course WHERE \"name\"= ");
            sb.append("\"");
            sb.append(courseName);
            sb.append("\"");
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
        }
    }

    public List<TimePerDay> getAllTimePerDay(){
        List<TimePerDay> tpdList = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM timePerDay");
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate localDate = LocalDate.parse(resultSet.getString("date"));
                localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(localDate);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setHours(LocalTime.parse(resultSet.getString("hours"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                tpdList.add(time);
            }
            return tpdList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public void insertHoursForToday(LocalTime hours, int course_id){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO timePerDay (\"date\", \"hours\", \"course_id\") VALUES (\"");
        sb.append(LocalDate.now());
        sb.append("\", \"");
        if(hours != null)
            sb.append(hours);
        else {
            logger.log(Level.WARNING, "hours = null");
        }
        sb.append("\", \"");
        sb.append(course_id);
        sb.append("\")");
        try (Statement statement = connection.createStatement()) {
            statement.execute(sb.toString());
            logger.log(Level.INFO, sb.toString());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public List<TimePerDay> getTimePerDayForDayAndCourseId(int course_id, LocalDate localDate) {
        List<TimePerDay> tpdList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM timePerDay WHERE course_id=");
        sb.append(course_id);
        sb.append(" AND date=\"");
        sb.append(localDate.toString());
        sb.append("\"");
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sb.toString());
            while (resultSet.next()) {
                TimePerDay time = new TimePerDay();
                time.setId(resultSet.getInt("id"));
                LocalDate lclDt = LocalDate.parse(resultSet.getString("date"));
                lclDt.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                time.setDate(lclDt);
                time.setCourse_id(resultSet.getInt("course_id"));
                time.setHours(LocalTime.parse(resultSet.getString("hours"), DateTimeFormatter.ofPattern("HH:mm:ss")));

                tpdList.add(time);
            }
            return tpdList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}
