package com.studyhelper.db.source;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateNewSchema {

    private final Logger logger;
    private Connection connection;

    public CreateNewSchema() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(CreateNewSchema.class.getName());
    }

    protected void createDatabaseSchema() {
        try{
            createCourseTable();
            createTimeTable();
            createTimePerDayTable();
            createTodoTable();
            createMotivationTable();

            logger.log(Level.INFO, "Creating schemas successfully finished.");
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void execute(StringBuilder stringBuilder) throws SQLException{
        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
        }
    }

    private void createMotivationTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"motivation\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"title\" TEXT NOT NULL,\n" +
                "        \"body\"  TEXT DEFAULT NULL,\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");

        execute(stringBuilder);
    }

    private void createTodoTable() throws SQLException {
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

    private void createTimePerDayTable() throws SQLException {
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

    private void createTimeTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS \"time\" (\n" +
                "        \"id\"    INTEGER NOT NULL,\n" +
                "        \"duration\"      TEXT NOT NULL DEFAULT 'time(\"PT0S\")',\n" +
                "        \"course_id\"     INTEGER NOT NULL,\n" +
                "        FOREIGN KEY(\"course_id\") REFERENCES \"course\"(\"id\"),\n" +
                "        PRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");");
        execute(stringBuilder);
    }

    private void createCourseTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
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
