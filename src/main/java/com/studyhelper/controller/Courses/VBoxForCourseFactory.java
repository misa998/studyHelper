package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Time;
import com.studyhelper.db.model.Time.TimeServiceImpl;
import com.studyhelper.db.properties.I18N;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.time.Duration;
import java.time.LocalDate;

/*
*   this class holds all the logic for
*   VBox for course
 */
public class VBoxForCourseFactory {
    private VBoxForCourse vBoxForCourse;
    private ChangeListener<String> textFieldListener;

    public VBoxForCourseFactory(VBoxForCourse vBox) {
        this.vBoxForCourse = vBox;
    }

    public VBoxForCourse getvBoxForCourse(){
        return this.vBoxForCourse;
    }

    public void createVbox(Course course){
        vBoxForCourse.setCourse(course);
        vBoxForCourse.setvBox(vBoxSetup(course.getName()));
        vBoxForCourse.setNameTextField(textFieldSetup(course.getName()));
        vBoxForCourse.setLabelForHours(hoursLabelSetup(course.getId()));
        vBoxForCourse.setLabelForDue(dueLabelSetup(course.getDue()));

        vBoxForCourse.getvBox().getChildren().addAll(
                vBoxForCourse.getNameTextField(),
                vBoxForCourse.getLabelForHours(),
                vBoxForCourse.getLabelForDue()
        );
    }

    private VBox vBoxSetup(String courseName) {
        VBox newVbox = new VBox();
        newVbox.setId(courseName);
        newVbox.setStyle(vBoxForCourse.getvBoxModel().getStyle());
        newVbox.setEffect(vBoxForCourse.getvBoxModel().getEffect());
        newVbox.setPrefSize(vBoxForCourse.getvBoxModel().getPrefWidth(), vBoxForCourse.getvBoxModel().getPrefHeight());
        newVbox.setSpacing(10);
        newVbox.setAlignment(vBoxForCourse.getvBoxModel().getAlignment());
        newVbox.setOnMouseClicked(vBoxForCourse.getvBoxModel().getOnMouseClicked());
        newVbox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                newVbox.setStyle(hoverStyleAfter());
            else
                newVbox.setStyle(hoverStyleBefore());
        });

        return newVbox;
    }

    private String hoverStyleAfter() {
        return "-fx-background-color : #524A7B;" +
                "-fx-background-radius : 20;";
    }

    private String hoverStyleBefore() {
        return "-fx-background-color : #393351;" +
                "-fx-background-radius : 20;";
    }

    private TextField textFieldSetup(String name) {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color : transparent; " +
                "-fx-text-fill : #cdcdcd; -fx-alignment : center;");
        textField.setText(name);
        textField.textProperty().addListener(textFieldListener);
        return textField;
    }

    public void setListenerForTextField(ChangeListener<String> listener) {
        this.textFieldListener = listener;
    }

    private Label dueLabelSetup(LocalDate due) {
        Label label2 = new Label();
        label2.setWrapText(true);
        long daysLeft = Duration.between(LocalDate.now().atStartOfDay(), due.atStartOfDay()).toDays();
        String daysLeftString;
        if(daysLeft <= 0){
            daysLeftString = " " + I18N.getString("courseList.due.label.expired");
            label2.setTextFill(Paint.valueOf("#cdcdcd"));
        } else if(daysLeft < 10){
            daysLeftString = daysLeft + " " + I18N.getString("courseList.due.label.left");
            label2.setTextFill(Paint.valueOf("#bf7878"));
        }
        else{
            daysLeftString = daysLeft + " " + I18N.getString("courseList.due.label.left");
            label2.setTextFill(Paint.valueOf("#8dae72"));
        }
        label2.setText(daysLeftString);

        return label2;
    }

    private Label hoursLabelSetup(int id) {
        Label label3 = new Label();
        label3.setTextFill(Paint.valueOf("#cdcdcd"));
        label3.setWrapText(true);
        Time time = new TimeServiceImpl().get().byCourseId(id);
        if(time == null)
            label3.setText("N/A");
        else
            label3.setText(time.getDuration().toHours() + " " + I18N.getString("courseList.hours.label"));

        return label3;
    }
}
