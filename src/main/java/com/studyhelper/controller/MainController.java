package com.studyhelper.controller;

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

    private final URL dashboardFXMLPath = getClass().getResource("/ui/dashboard.fxml");
    private final URL courseOverviewFXMLPath = getClass().getResource("/ui/courseOverview.fxml");
    private final URL pomodoroFXMLPath = getClass().getResource("/ui/pomodoro.fxml");

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