package com.studyhelper.controller;

import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiProperties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabsPaneController {
    @FXML
    private AnchorPane dashboardTabAnchorPane;
    @FXML
    private AnchorPane coursesTabAnchorPane;
    @FXML
    private AnchorPane pomodoroTabAnchorPane;
    @FXML
    private AnchorPane motivationTabAnchorPane;

    private final Logger logger = Logger.getLogger(TabsPaneController.class.getName());
    private UiProperties uiProperties = new UiProperties();

    private AnchorPane dashboardAnchorPane = null;
    private AnchorPane coursesAnchorPane = null;
    private AnchorPane pomodoroAnchorPane = null;
    private AnchorPane motivationAnchorPane = null;

    @FXML
    public void courseTabChange(){
        if(coursesAnchorPane == null)
            loadCourseTab();
    }

    private void loadCourseTab() {
        try {
            coursesAnchorPane = FXMLLoader.load(uiProperties.getResourceURL("courseOverviewFXMLPath"), I18N.getResourceBundle());
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
            dashboardAnchorPane = FXMLLoader.load(uiProperties.getResourceURL("dashboardFXMLPath"), I18N.getResourceBundle());
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
            pomodoroAnchorPane = FXMLLoader.load(uiProperties.getResourceURL("pomodoroFXMLPath"), I18N.getResourceBundle());
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
            motivationAnchorPane = FXMLLoader.load(uiProperties.getResourceURL("motivationFXMLPath"), I18N.getResourceBundle());
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }

        motivationTabAnchorPane.getChildren().setAll(motivationAnchorPane);
    }
}
