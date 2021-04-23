package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.model.CourseServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerAddCourseDialog {
    @FXML
    private TextField editName;
    @FXML
    private TextArea editDesc;
    @FXML
    private DatePicker editDue;
    @FXML
    private Button addCourse;

    public void initialize(){
        addCourse.setVisible(false);
        buttonBindings();
    }

    private void buttonBindings(){
        addCourse.disableProperty().bind(
                Bindings.selectBoolean(editName.textProperty().isEmpty())
                        .or(Bindings.selectBoolean(editDesc.textProperty().isEmpty()))
                        .or(Bindings.selectBoolean(editDue.valueProperty().isNull()))
        );
    }

    public Course getData(){
        if(addCourse.isDisabled())
            return null;

        return new Course(0, editName.getText(), editDesc.getText(), editDue.getValue());
    }
}
