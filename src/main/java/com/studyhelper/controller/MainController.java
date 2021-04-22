package com.studyhelper.controller;

import com.studyhelper.db.properties.UiProperties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

	// https://edencoding.com/scene-background/
    @FXML
    private AnchorPane dashboardTabAnchorPane;
    @FXML
    private AnchorPane coursesTabAnchorPane;
    @FXML
    private AnchorPane pomodoroTabAnchorPane;
    @FXML
    private AnchorPane motivationTabAnchorPane;
    @FXML
    private BorderPane mainBorderPane;

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    private final URL dashboardFXMLPath = new UiProperties().getDashboardFXMLPath();
    private final URL courseOverviewFXMLPath = new UiProperties().getCourseOverviewFXMLPath();
    private final URL pomodoroFXMLPath = new UiProperties().getPomodoroFXMLPath();
    private final URL motivationFXMLPath = new UiProperties().getMotivationFXMLPath();


    private AnchorPane dashboardAnchorPane = null;
    private AnchorPane coursesAnchorPane = null;
    private AnchorPane pomodoroAnchorPane = null;
    private AnchorPane motivationAnchorPane = null;

    public void initialize(){

    }

    @FXML
    public void courseTabChange(){
        if(coursesAnchorPane == null)
            loadCourseTab();
    }

    private void loadCourseTab() {
        try {
            coursesAnchorPane = FXMLLoader.load(courseOverviewFXMLPath);
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
        coursesTabAnchorPane.getChildren().setAll(coursesAnchorPane);
    }

    @FXML
    public void dashboardTabChanged(){
        if(dashboardAnchorPane == null)
            loadDashboardTab();
    }

    private void loadDashboardTab() {
        try {
            dashboardAnchorPane = FXMLLoader.load(dashboardFXMLPath);
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }

        dashboardTabAnchorPane.getChildren().setAll(dashboardAnchorPane);
    }

    @FXML
    public void pomodoroTabChange() {
        if(pomodoroAnchorPane == null)
            loadPomodoroTab();
    }

    private void loadPomodoroTab() {
        try {
            pomodoroAnchorPane = FXMLLoader.load(pomodoroFXMLPath);
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }

        pomodoroTabAnchorPane.getChildren().setAll(pomodoroAnchorPane);
    }

    @FXML
    public void motivationTabChanged() {
        if(motivationAnchorPane == null)
            loadMotivationTab();
    }

    private void loadMotivationTab() {
        try {
            motivationAnchorPane = FXMLLoader.load(motivationFXMLPath);
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }

        motivationTabAnchorPane.getChildren().setAll(motivationAnchorPane);
    }

    @FXML
    public void onActionAboutMenuItem(){
        Dialog<ButtonType> aboutDialog = new Dialog<>();
//        aboutDialog.initOwner(mainBorderPane.getScene().getWindow());
        aboutDialog.setTitle("About");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getAboutFXMLPath());
        try{
            aboutDialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
        aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        aboutDialog.showAndWait();
    }
}