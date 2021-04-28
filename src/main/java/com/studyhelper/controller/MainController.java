package com.studyhelper.controller;

import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiProperties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;

import java.io.IOException;
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
    private UiProperties uiProperties = new UiProperties();

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

    @FXML
    public void onActionAboutMenuItem(){
        try {
            getDialogConfig().showAndWait();
        } catch (IOException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private Dialog<ButtonType> getDialogConfig() throws IOException {
        Dialog<ButtonType> aboutDialog = new Dialog<>();
//        aboutDialog.initOwner(mainBorderPane.getScene().getWindow());
        aboutDialog.setTitle("About");
        aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        aboutDialog.getDialogPane().setContent(getFXMLLoader().load());

        return aboutDialog;
    }

    private FXMLLoader getFXMLLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(uiProperties.getResourceURL("aboutFXMLPath"));
        return fxmlLoader;
    }
}