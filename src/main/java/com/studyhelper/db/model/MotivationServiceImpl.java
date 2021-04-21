package com.studyhelper.db.model;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationServiceImpl implements MotivationService{

    private final Logger logger;
    private Connection connection;

    public MotivationServiceImpl() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationServiceImpl.class.getName());
    }

    @Override
    public ObservableList<Motivation> getAllMotivation() {
        try{
            return getAllMotivationExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Motivation> getAllMotivationExecute() throws SQLException{
        ObservableList<Motivation> motivationObservableList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM motivation");

            while (resultSet.next())
                motivationObservableList.add(getMotivationFromResultSet(resultSet));

            return motivationObservableList;
        }
    }

    @Override
    public Motivation getMotivationById(int id) {
        try{
            return getMotivationByIdExecute(id);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Motivation getMotivationByIdExecute(int id) throws SQLException {
        PreparedStatement getMotivationById = connection.prepareStatement(
                "SELECT * FROM motivation WHERE id= ?"
        );
        getMotivationById.setInt(1, id);
        ResultSet resultSet = getMotivationById.executeQuery();

        return getMotivationFromResultSet(resultSet);
    }

    private Motivation getMotivationFromResultSet(ResultSet resultSet) throws SQLException {
        Motivation motivation = new Motivation();
        motivation.setId(resultSet.getInt("id"));
        motivation.setTitle(resultSet.getString("title"));
        motivation.setBody(resultSet.getString("body"));

        return motivation;
    }

    @Override
    public Motivation getMotivationByTitle(String title) {
        try{
            return getMotivationByTitleExecute(title);
        } catch (SQLException e){
            logger.log(Level.SEVERE,
                    "Motivation with that title doesn't exist. Returning null.");
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Motivation getMotivationByTitleExecute(String title) throws SQLException {
        PreparedStatement getMotivationByTitle = connection.prepareStatement(
                "SELECT * FROM motivation WHERE title= ?"
        );
        getMotivationByTitle.setString(1, title);
        ResultSet resultSet = getMotivationByTitle.executeQuery();

        return getMotivationFromResultSet(resultSet);
    }


    @Override
    public Motivation getMotivationByBody(String body) {
        try{
            return getMotivationByBodyExecute(body);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Motivation getMotivationByBodyExecute(String body) throws SQLException {
        PreparedStatement getMotivationByBody = connection.prepareStatement(
                "SELECT * FROM motivation WHERE body= ?"
        );
        getMotivationByBody.setString(1, body);
        ResultSet resultSet = getMotivationByBody.executeQuery();

        return getMotivationFromResultSet(resultSet);
    }

    @Override
    public void updateMotivationTitle(String title, int id) {
        try {
            updateMotivationTitleExecute(title, id);
        }
         catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateMotivationTitleExecute(String title, int id) throws SQLException {
        PreparedStatement updateMotivationTitle = connection.prepareStatement(
                "UPDATE motivation SET title= ? WHERE id= ?"
        );
        updateMotivationTitle.setString(1, title);
        updateMotivationTitle.setInt(2, id);
        int affectedRecords = updateMotivationTitle.executeUpdate();

        isUpdated(affectedRecords);
    }

    @Override
    public void updateMotivationBody(String body, int id) {
        try {
            updateMotivationBodyExecute(body, id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void updateMotivationBodyExecute(String body, int id) throws SQLException {
        PreparedStatement updateMotivationBody = connection.prepareStatement(
                "UPDATE motivation SET body= ? WHERE id= ?"
        );
        updateMotivationBody.setString(1, body);
        updateMotivationBody.setInt(2, id);
        int affectedRecords = updateMotivationBody.executeUpdate();

        isUpdated(affectedRecords);
    }


    private void isUpdated(int affectedRecords) throws SQLException {
        if(affectedRecords != 1)
            throw new SQLException("Non affected");
    }

    @Override
    public void deleteMotivationById(int id) {
        try {
            deleteMotivationByIdExecute(id);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private void deleteMotivationByIdExecute(int id) throws SQLException {
        PreparedStatement deleteMotivationById = connection.prepareStatement(
                "DELETE FROM motivation WHERE id= ?"
        );
        deleteMotivationById.setInt(1, id);
        int affectedRows = deleteMotivationById.executeUpdate();
        isUpdated(affectedRows);

    }

    @Override
    public int insertMotivation(Motivation motivation) {
        try {
            return insertMotivationExecute(motivation);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return -1;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private int insertMotivationExecute(Motivation motivation) throws SQLException {
        PreparedStatement insertMotivation = connection.prepareStatement(
                "INSERT INTO motivation (title, body) VALUES (?, ?)"
        );
        insertMotivation.setString(1, motivation.getTitle());
        insertMotivation.setString(2, motivation.getBody());
        int affectedRows = insertMotivation.executeUpdate();

        isUpdated(affectedRows);

        return anIdOfNewMotivation(insertMotivation);
    }


    private int anIdOfNewMotivation(PreparedStatement insertMotivation) throws SQLException {
        ResultSet generatedKeys = insertMotivation.getGeneratedKeys();
        if(generatedKeys.next())
            return generatedKeys.getInt(1);
         else
            throw new SQLException("Error");
    }
}
