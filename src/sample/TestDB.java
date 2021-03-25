package sample;

import java.sql.*;

public class TestDB {
    public static final String DB_NAME = "studymng.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Java\\projects\\StudyHelperDB\\" + DB_NAME;

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement()){
            //statement.execute("select * from student");
            //ResultSet resultSet = statement.getResultSet();
            ResultSet resultSet = statement.executeQuery("select time.id, time.period, course.name from time inner join course on course.id = time.course_id");
            while(resultSet.next()){
                System.out.println(resultSet.getInt("id") + " " +
                                    resultSet.getString("period") + " " +
                                    resultSet.getString("name"));
            }

        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
