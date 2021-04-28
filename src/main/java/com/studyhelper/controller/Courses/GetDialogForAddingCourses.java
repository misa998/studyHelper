package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiProperties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

import java.io.IOException;

public class GetDialogForAddingCourses {
    private Dialog<Course> insertDialog;

    public GetDialogForAddingCourses() {
        this.insertDialog = new Dialog<>();
    }

    public Dialog<Course> getInsertDialog() {
        return insertDialog;
    }

    public void configureDialog() throws IOException {
        String title = I18N.getString("addCourse.dialog.title");
        insertDialog.setTitle(title);
        insertDialog.getDialogPane().setContent(getLoaderForAddCourse().load());
        insertDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        closeButtonConfig(insertDialog.getDialogPane().lookupButton(ButtonType.CLOSE));
    }

    private FXMLLoader getLoaderForAddCourse(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getResourceURL("addCourseFXMLPath"));
        fxmlLoader.setResources(I18N.getResourceBundle());
        return fxmlLoader;
    }

    private void closeButtonConfig(Node close){
        close.managedProperty().bind(close.visibleProperty());
        close.setVisible(false);
    }

    public void setInitOwner(Window window) {
        insertDialog.initOwner(window);
    }
}
