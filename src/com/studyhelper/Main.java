package com.studyhelper;

import com.studyhelper.controller.TrayIconController;
import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.source.DataSource;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static final String IMAGE_URL = "file:src/com/studyhelper/ui/resources/main-icon-black-white-128.png";
    private final String MAIN_FXML_LOCATION = "/com/studyhelper/ui/main.fxml";
    private final String WINDOW_TITLE = "Your study helper";
    private final Point mainWindowDimensions = new Point(840, 600);

    @Override
    public void start(Stage primaryStage) {
        try {
            configureStage(primaryStage);
        } catch (Exception e){
            showErrorMessage();
        }
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
        stage.getIcons().add(new Image(IMAGE_URL));
        stage.show();
    }

    public static void showMinimizedStage(){
        System.out.println(stage.iconifiedProperty().get());
        stage.setIconified(false);
        System.out.println(stage.iconifiedProperty().get());
    }

    @Override
    public void init() throws Exception {
        super.init();
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
