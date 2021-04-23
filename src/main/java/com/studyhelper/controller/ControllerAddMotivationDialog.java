package com.studyhelper.controller;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.MotivationServiceImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControllerAddMotivationDialog {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea bodyTextArea;
    @FXML
    private Button addButton;

    public void initialize(){
        setupAddButton();
    }

    private void setupAddButton() {
        addButton.disableProperty().bind(
                titleTextField.textProperty().isEmpty()
        );
    }

    @FXML
    public void onActionAdd(ActionEvent ae){
        insertData();
        closeWindow(ae);
    }

    public void insertData(){
        Motivation motivation = new Motivation(0, titleTextField.getText(), bodyTextArea.getText());
        int id = new MotivationServiceImpl().insertMotivation(motivation);
    }

    private void closeWindow(ActionEvent ae) {
        Node source = (Node)  ae.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.close();
    }

}