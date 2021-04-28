package com.studyhelper.db.model.Motivation;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.source.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotivationGet implements MotivationGetService{

    private final Logger logger;
    private Connection connection;

    public MotivationGet() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationGet.class.getName());
    }

    @Override
    public Motivation byId(int id) {
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
    public Motivation byTitle(String title) {
        try{
            return byTitleExecute(title);
        } catch (SQLException e){
            logger.log(Level.SEVERE,
                    "Motivation with that title doesn't exist. Returning null.");
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Motivation byTitleExecute(String title) throws SQLException {
        PreparedStatement getMotivationByTitle = connection.prepareStatement(
                "SELECT * FROM motivation WHERE title= ?"
        );
        getMotivationByTitle.setString(1, title);
        ResultSet resultSet = getMotivationByTitle.executeQuery();

        return getMotivationFromResultSet(resultSet);
    }

    @Override
    public Motivation byBody(String body) {
        try{
            return byBodyExecute(body);
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private Motivation byBodyExecute(String body) throws SQLException {
        PreparedStatement getMotivationByBody = connection.prepareStatement(
                "SELECT * FROM motivation WHERE body= ?"
        );
        getMotivationByBody.setString(1, body);
        ResultSet resultSet = getMotivationByBody.executeQuery();

        return getMotivationFromResultSet(resultSet);
    }
}
