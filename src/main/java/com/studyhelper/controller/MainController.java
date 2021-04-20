package com.studyhelper.controller;

import com.studyhelper.db.properties.UiProperties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
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

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    private final URL dashboardFXMLPath = new UiProperties().getDashboardFXMLPath();
    private final URL courseOverviewFXMLPath = new UiProperties().getCourseOverviewFXMLPath();
    private final URL pomodoroFXMLPath = new UiProperties().getPomodoroFXMLPath();

    private AnchorPane dashboardAnchorPane = null;
    private AnchorPane coursesAnchorPane = null;
    private AnchorPane pomodoroAnchorPane = null;

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
}