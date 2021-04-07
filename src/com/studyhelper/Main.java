package com.studyhelper;

import com.studyhelper.controller.TrayIconController;
import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.source.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;

public class Main extends Application {

    private static final String IMAGE_URL = "file:com.studyhelper.ui\\resources\\icon.png";
    private final String MAIN_FXML_LOCATION = "/com/studyhelper/ui/main.fxml";
    private final String WINDOW_TITLE = "Your study helper";
    private final Point mainWindowDimensions = new Point(840, 600);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent rootWindow = FXMLLoader.load(getClass().getResource(MAIN_FXML_LOCATION));
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(rootWindow, mainWindowDimensions.x, mainWindowDimensions.y));
        primaryStage.getIcons().add(new Image(IMAGE_URL));
        primaryStage.show();
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
