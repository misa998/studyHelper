package com.studyhelper.db.model;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationServiceImpl implements MotivationService{

    private final Logger logger = Logger.getLogger(MotivationServiceImpl.class.getName());
    private Connection connection = null;

    @Override
    public ObservableList<Motivation> getAllMotivation() {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        ObservableList<Motivation> motivationObservableList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM motivation");

            while (resultSet.next()) {
                Motivation motivation = new Motivation();
                motivation.setId(resultSet.getInt("id"));
                motivation.setTitle(resultSet.getString("title"));
                motivation.setBody(resultSet.getString("body"));

                motivationObservableList.add(motivation);
            }

            return motivationObservableList;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public Motivation getMotivationById(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM motivation WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            Motivation motivation = new Motivation();
            motivation.setId(resultSet.getInt("id"));
            motivation.setTitle(resultSet.getString("title"));
            motivation.setBody(resultSet.getString("body"));

            return motivation;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public Motivation getMotivationByTitle(String title) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM motivation WHERE title=");
        stringBuilder.append("\"");
        stringBuilder.append(title);
        stringBuilder.append("\"");

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            Motivation motivation = new Motivation();
            motivation.setId(resultSet.getInt("id"));
            motivation.setTitle(resultSet.getString("title"));
            motivation.setBody(resultSet.getString("body"));

            return motivation;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public Motivation getMotivationByBody(String body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM motivation WHERE body=");
        stringBuilder.append("\"");
        stringBuilder.append(body);
        stringBuilder.append("\"");

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return null;

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            Motivation motivation = new Motivation();
            motivation.setId(resultSet.getInt("id"));
            motivation.setTitle(resultSet.getString("title"));
            motivation.setBody(resultSet.getString("body"));

            return motivation;
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void updateMotivationTitle(String title, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE motivation SET title=\"");
        stringBuilder.append(title);
        stringBuilder.append("\" ");
        stringBuilder.append("WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void updateMotivationBody(String body, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE motivation SET body=\"");
        stringBuilder.append(body);
        stringBuilder.append("\" ");
        stringBuilder.append("WHERE id=");
        stringBuilder.append(id);

        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            logger.log(Level.INFO, stringBuilder.toString());

            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void deleteMotivationById(int id) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM motivation WHERE id=");
        stringBuilder.append(id);
        logger.log(Level.INFO, stringBuilder.toString());

        try(Statement statement = connection.createStatement()) {
            statement.executeQuery(stringBuilder.toString());

        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public void insertMotivation(Motivation motivation) {
        connection = DataSource.getInstance().openConnection();
        if(connection == null)
            return;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO motivation (\"title\", \"body\") VALUES (\"");
        stringBuilder.append(motivation.getTitle());
        stringBuilder.append("\", \"");
        stringBuilder.append(motivation.getBody());
        stringBuilder.append("\")");

        logger.log(Level.INFO, stringBuilder.toString());

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());

            logger.log(Level.INFO, stringBuilder.toString());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return;
        } finally {
            connection = null;
            DataSource.getInstance().closeConnection();
        }
    }
}
