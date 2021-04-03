package com.studyhelper;

import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.source.DataSource;
import com.studyhelper.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/studyhelper/ui/sample.fxml"));
        primaryStage.setTitle("Your study helper");
        primaryStage.setScene(new Scene(root, 840, 600));
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
        Controller.closeTray();
        if(PomodoroServiceImpl.getInstance().getState() == PomodoroServiceImpl.State.STUDY)
            PomodoroServiceImpl.getInstance().endStudySession(null);
        DataSource.getInstance().closeConnection();
        super.stop();
    }


    public static void main(String[] args){
        launch(args);
    }
}
