package com.studyhelper.controller;

import com.studyhelper.db.entity.Motivation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class ControllerAddMotivationDialog {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea bodyTextArea;

    public void initialize(){

    }

    public Motivation getData(){
        if(titleTextField.getText().isEmpty())
            return null;

        return new Motivation(0, titleTextField.getText(), bodyTextArea.getText());
    }
}
