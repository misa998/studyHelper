package com.studyhelper;

import com.studyhelper.controller.TrayIconController;
import com.studyhelper.db.model.Pomodoro.PomodoroServiceImpl;
import com.studyhelper.db.model.Pomodoro.PomodoroStudyStates;
import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiProperties;
import com.studyhelper.db.source.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.FileHandler;

public class Main extends Application {
    private Logger logger = Logger.getLogger(Main.class.getName());

    private static Stage stage = null;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        setup();
    }

    private void setup() {
        setupLogger();
        new TrayIconController().showTray();
        DataSource.getInstance().firstConnection();
    }

    private void setupLogger() {
        try {
            setupLoggerHandler();
        } catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void setupLoggerHandler() throws IOException {
        createDir();
        Handler handler = new FileHandler("log\\errorLog.xml",
                1000000, 3, true);
        Logger.getLogger("").addHandler(handler);
    }

    private void createDir() {
        File file = new File("log");
        file.mkdir();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            configureStage(primaryStage);
        } catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
            showErrorMessage();
        }
    }

    private void configureStage(Stage primaryStage) throws Exception {
        Parent rootWindow = getMainControllerLoader().load();
        stage = primaryStage;
        stage.setTitle(I18N.getString("title"));
        stage.setResizable(false);
        stage.setScene(new Scene(rootWindow, 840, 600));
        stage.getIcons().add(new Image(String.valueOf(
                new UiProperties().getResourceURL("mainImage"))));
        stage.show();
    }

    private FXMLLoader getMainControllerLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getResourceURL("mainFXMLPath"));
        fxmlLoader.setResources(I18N.getResourceBundle());
        return fxmlLoader;
    }

    private void showErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error while loading the app.");
        alert.show();
    }

    public static void showMinimizedStage(){
        stage.setIconified(false);
    }

    @Override
    public void stop() throws Exception {
        TrayIconController.closeSystemTray();
        stopPomodoro();
        super.stop();
    }

    private void stopPomodoro() {
        if(PomodoroStudyStates.studyStateProperty.get()
                == PomodoroStudyStates.StudyState.STUDY) {
            PomodoroServiceImpl.getInstance().endStudySession();
            logger.log(Level.SEVERE, "Window closed while state is STUDY");
        }
        DataSource.getInstance().closeConnection();
    }
}
