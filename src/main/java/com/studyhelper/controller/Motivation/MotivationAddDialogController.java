package com.studyhelper.controller.Motivation;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.Motivation.MotivationServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MotivationAddDialogController {
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
        int id = new MotivationServiceImpl().insert().add(motivation);
    }

    private void closeWindow(ActionEvent ae) {
        Node source = (Node)  ae.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.close();
    }

}