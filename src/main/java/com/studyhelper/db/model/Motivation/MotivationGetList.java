package com.studyhelper.db.model.Motivation;

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

public class MotivationGetList implements MotivationGetListService {

    private final Logger logger;
    private Connection connection;

    public MotivationGetList() {
        this.connection = DataSource.getInstance().openConnection();
        this.logger = Logger.getLogger(MotivationGetList.class.getName());
    }

    @Override
    public ObservableList<Motivation> all() {
        try{
            return getAllExecute();
        } catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
            return FXCollections.emptyObservableList();
        } finally {
            DataSource.getInstance().closeConnection();
        }
    }

    private ObservableList<Motivation> getAllExecute() throws SQLException{
        ObservableList<Motivation> motivationObservableList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM motivation");

            while (resultSet.next())
                motivationObservableList.add(getMotivationFromResultSet(resultSet));

            return motivationObservableList;
        }
    }

    private Motivation getMotivationFromResultSet(ResultSet resultSet) throws SQLException {
        Motivation motivation = new Motivation();
        motivation.setId(resultSet.getInt("id"));
        motivation.setTitle(resultSet.getString("title"));
        motivation.setBody(resultSet.getString("body"));

        return motivation;
    }
}
