package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CourseAddDialogController {
    @FXML
    private TextField editName;
    @FXML
    private TextArea editDesc;
    @FXML
    private DatePicker editDue;
    @FXML
    private Button addCourse;

    public void initialize(){
        buttonBindings();
    }

    private void buttonBindings(){
        addCourse.disableProperty().bind(
                Bindings.selectBoolean(editName.textProperty().isEmpty())
                        .or(Bindings.selectBoolean(editDesc.textProperty().isEmpty()))
                        .or(Bindings.selectBoolean(editDue.valueProperty().isNull()))
        );
    }

    @FXML
    public void onActionAdd(ActionEvent ae){
        insertData();
        closeDialog(ae);
    }

    private void closeDialog(ActionEvent ae) {
        Node source = (Node)  ae.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void insertData() {
        new CourseServiceImpl().insert().add(getData());
    }

    public Course getData(){
        return new Course(0, editName.getText(), editDesc.getText(), editDue.getValue());
    }
}
