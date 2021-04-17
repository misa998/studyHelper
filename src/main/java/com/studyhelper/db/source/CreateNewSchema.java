package com.studyhelper.db.source;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateNewSchema {

    private final Logger logger = Logger.getLogger(CreateNewSchema.class.getName());
    private Connection connection;

    protected void createDatabaseSchema() {
        try{
            createCourseTable();
            createTimeTable();
            createTimePerDayTable();
            createTodoTable();
            createMotivationTable();
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void getConnection(){
        connection = DataSource.getInstance().openConnection();
    }

    private void logRequest(String request){
        logger.log(Level.INFO, request);
    }

    private void execute(StringBuilder stringBuilder){
        logRequest(stringBuilder.toString());

        getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    private void createMotivationTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"motivation\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"title\" TEXT NOT NULL,\n" +
                "        \"body\"  TEXT DEFAULT NULL,\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");

        execute(stringBuilder);
    }

    private void createTodoTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"todo\" (\n" +
                "        \"id\"    INTEGER,\n" +
                "        \"completed\"     INTEGER NOT NULL DEFAULT 0,\n" +
                "        \"item\"  TEXT NOT NULL,\n" +
                "        \"course_id\"     INTEGER NOT NULL,\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT),\n" +
                "        FOREIGN KEY(\"course_id\") REFERENCES \"course\"(\"id\")\n" +
                ");");
        execute(stringBuilder);
    }

    private void createTimePerDayTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"timePerDay\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"date\"  TEXT NOT NULL,\n" +
                "        \"duration\"      TEXT NOT NULL,\n" +
                "        \"course_id\"     INTEGER NOT NULL,\n" +
                "        FOREIGN KEY(\"course_id\") REFERENCES \"course\"(\"id\"),\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");
        execute(stringBuilder);
    }

    private void createTimeTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"time\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"duration\"      TEXT NOT NULL DEFAULT 'time(\"00:00\")',\n" +
                "        \"course_id\"     INTEGER NOT NULL,\n" +
                "        FOREIGN KEY(\"course_id\") REFERENCES \"course\"(\"id\"),\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");
        execute(stringBuilder);
    }

    private void createCourseTable() {
        StringBuilder stringBuilder = new StringBuilder();
        /*stringBuilder.append("CREATE TABLE \"course\" (" +
                "\"id\" int NOT NULL AUTO_INCREMENT, " +
                "\"name\" varchar(45) NOT NULL, " +
                "\"description\" tinytext, " +
                "\"due\" time NOT NULL, " +
                "\"student_id\" int NOT NULL, " +
                "PRIMARY KEY (\"id\"), " +
                "UNIQUE KEY \"name_UNIQUE\" (\"name\"), " +
                "KEY \"id_idx\" (\"student_id\"), " +
                "CONSTRAINT \"id\" FOREIGN KEY (\"student_id\") REFERENCES \"student\" (\"id\") " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");*/
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"course\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"name\"  TEXT NOT NULL UNIQUE,\n" +
                "        \"description\"   TEXT DEFAULT NULL,\n" +
                "        \"due\"   TEXT NOT NULL,\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");
        execute(stringBuilder);
    }
}
