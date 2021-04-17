package com.studyhelper;

import com.studyhelper.controller.TrayIconController;
import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.source.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main extends Application {

    private static final String IMAGE_URL = "/ui/icons/main-icon-black-white-128.png";
    private final String MAIN_FXML_LOCATION = "/ui/main.fxml";
    private final String WINDOW_TITLE = "Your study helper";
    private final String LOG_FILE = "log.xml";
    private final Point mainWindowDimensions = new Point(840, 600);

    @Override
    public void start(Stage primaryStage) {
        try {
            configureStage(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
            showErrorMessage();
        }
    }

    private void initialize() {
        try {
            setupLogger();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setupLogger() throws IOException {
        Handler handler = new FileHandler(LOG_FILE, true);
        Logger.getLogger("").addHandler(handler);
    }

    private void showErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error while loading the app. Check fxml location.");
        alert.show();
    }

    private static Stage stage = null;

    private void configureStage(Stage primaryStage) throws Exception {
        Parent rootWindow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(MAIN_FXML_LOCATION)));
        stage = primaryStage;
        stage.setTitle(WINDOW_TITLE);
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootWindow, mainWindowDimensions.x, mainWindowDimensions.y));
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource(IMAGE_URL))));
        stage.show();
    }

    public static void showMinimizedStage(){
        stage.setIconified(false);
    }

    @Override
    public void init() throws Exception {
        super.init();
        initialize();
        DataSource.getInstance().firstConnection();
        /*if(!DataSource.getInstance().openConnection()){
            System.out.println("Error in connection");
            Platform.exit();
        }*/
    }

    /*
        # properly shutting down the app
        # study session will be ended and added to the db, only if it's already running
     */
    @Override
    public void stop() throws Exception {
        TrayIconController.closeSystemTray();
        if(PomodoroServiceImpl.getInstance().getStudyState() == PomodoroServiceImpl.StudyState.STUDY)
            PomodoroServiceImpl.getInstance().endStudySession();
        DataSource.getInstance().closeConnection();
        super.stop();
    }


    public static void main(String[] args){
        launch(args);
    }
}
