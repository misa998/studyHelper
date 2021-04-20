package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.MotivationServiceImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MotivationController {
    @FXML
    private Button addMotivation;
    @FXML
    private VBox vBoxMotivation;
    @FXML
    private VBox vBoxEachMotivation;

    private IntegerProperty selectedMotivation = new SimpleIntegerProperty(0);

    public void initialize(){
        fillTheListOfMotivation();
    }

    private void fillTheListOfMotivation() {
        cleanOldDataForMotivationList();

        ObservableList<Motivation> motivations = new MotivationServiceImpl().getAllMotivation();

        for (Motivation motivation : motivations) {
            VBox vbox = setMotivationListVBox(motivation.getId());
            TextField textField = setMotivationListTextField(motivation.getTitle());
            TextArea textArea = setMotivationListTextArea(motivation.getBody());

            vbox.getChildren().addAll(textField, textArea);

            vBoxMotivation.getChildren().add(vbox);
        }
    }

    private TextArea setMotivationListTextArea(String body) {
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-background-color : transparent; " +
                "-fx-border-color : transparent;" +
                "-fx-background-radius : 20;" +
                "-fx-border-radius : 20;");
        textArea.setWrapText(true);
        textArea.setText(body);
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedMotivation.setValue(new MotivationServiceImpl().getMotivationByBody(oldValue).getId());
                new MotivationServiceImpl().updateMotivationBody(newValue, selectedMotivation.get());
            }
        });

        return textArea;
    }

    private TextField setMotivationListTextField(String title) {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color : transparent; -fx-font-weight: bold; -fx-font-size: 15; -fx-alignment : center;");
        textField.setText(title);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldTitle, String newTitle) {
                selectedMotivation.setValue(new MotivationServiceImpl().getMotivationByTitle(oldTitle).getId());
                new MotivationServiceImpl().updateMotivationTitle(newTitle, selectedMotivation.get());
            }
        });

        return textField;
    }

    private VBox setMotivationListVBox(int id) {
        VBox vbox = new VBox();
        vbox.setId(String.valueOf(id));
        vbox.setStyle(vBoxEachMotivation.getStyle());
        vbox.setEffect(vBoxEachMotivation.getEffect());
        vbox.setOnMouseClicked(vBoxEachMotivation.getOnMouseClicked());
        vbox.setPrefSize(vBoxEachMotivation.getPrefWidth(), vBoxEachMotivation.getPrefHeight());
        vbox.setSpacing(10);
        vbox.setAlignment(vBoxEachMotivation.getAlignment());

        return vbox;
    }

    @FXML
    private void vboxCoursesOnAction(MouseEvent mouseEvent){
        VBox vbox = (VBox) mouseEvent.getSource();
        Motivation motivation = new MotivationServiceImpl().getMotivationById(Integer.parseInt(vbox.getId()));
        if(motivation == null)
            return;
        selectedMotivation.setValue(motivation.getId());
    }

    private void cleanOldDataForMotivationList(){
        vBoxMotivation.getChildren().clear();
        vBoxEachMotivation.getChildren().clear();
    }

}
