package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.model.CourseServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ControllerEditPane {
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField editName;
    @FXML
    private TextArea editDesc;
    @FXML
    private DatePicker editDue;
    @FXML
    private Button addCourse;

    public void initialize(){
        addCourse.disableProperty().bind(
                Bindings.selectBoolean(editName.textProperty().isEmpty())
                .or(Bindings.selectBoolean(editDesc.textProperty().isEmpty()))
                .or(Bindings.selectBoolean(editDue.valueProperty().isNull()))
        );
    }

    @FXML
    private void addNewCourse(){
        new CourseServiceImpl().insertCourse(new Course(0, editName.getText(), editDesc.getText(), editDue.getValue()));
    }
}
