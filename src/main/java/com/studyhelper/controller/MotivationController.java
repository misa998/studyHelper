package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.CourseServiceImpl;
import com.studyhelper.db.model.MotivationServiceImpl;
import com.studyhelper.db.properties.UiProperties;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.script.Bindings;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

public class MotivationController {
    @FXML
    private Button addMotivation;
    @FXML
    private Button deleteSelectedMotivation;
    @FXML
    private VBox vBoxMotivation;
    @FXML
    private VBox vBoxEachMotivation;
    @FXML
    private AnchorPane moitvationAnchorPane;

    private IntegerProperty selectedMotivation = new SimpleIntegerProperty(0);

    public void initialize(){
        fillTheListOfMotivation();
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        deleteSelectedMotivation.disableProperty().bind(
                selectedMotivation.lessThan(1)
        );
    }

    private void fillTheListOfMotivation() {
        cleanOldDataForMotivationList();

        ObservableList<Motivation> motivations = new MotivationServiceImpl().getAllMotivation();

        for (Motivation motivation : motivations) {
            VBox vbox = createVBox(motivation);

            vBoxMotivation.getChildren().add(vbox);
        }
    }

    private VBox createVBox(Motivation motivation){
        VBox vbox = setMotivationListVBox(motivation.getId());
        TextField textField = setMotivationListTextField(motivation.getTitle(), motivation.getId());
        TextArea textArea = setMotivationListTextArea(motivation.getBody(), motivation.getId());

        vbox.getChildren().addAll(textField, textArea);
        return vbox;
    }

    private TextArea setMotivationListTextArea(String body, int id) {
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-background-color : transparent; " +
                "-fx-border-color : transparent;" +
                "-fx-background-radius : 20;" +
                "-fx-border-radius : 20;");
        textArea.setWrapText(true);
        textArea.setText(body);
        textArea.setPromptText("Content");
        textArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    selectedMotivation.setValue(id);
                    System.out.println("Expand");
//                    vBoxEachMotivation.setStyle("-fx-pref-height : 300;");
                }else{
                }
            }
        });
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                new MotivationServiceImpl().updateMotivationBody(newValue, selectedMotivation.get());
            }
        });

        return textArea;
    }

    private TextField setMotivationListTextField(String title, int id) {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color : transparent; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 15; " +
                "-fx-alignment : center;");
        textField.setText(title);
        textField.setPromptText("Title");
        textField.focusedProperty().addListener(e -> selectedMotivation.setValue(id));
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldTitle, String newTitle) {
                new MotivationServiceImpl().updateMotivationTitle(newTitle, id);
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
        vbox.setPadding(vBoxEachMotivation.getPadding());
        vbox.setPrefSize(vBoxEachMotivation.getPrefWidth(), vBoxEachMotivation.getPrefHeight());
        vbox.setSpacing(10);
        vbox.setAlignment(vBoxEachMotivation.getAlignment());

        return vbox;
    }

    @FXML
    private void vboxMotivationOnAction(MouseEvent mouseEvent){
        VBox vbox = (VBox) mouseEvent.getSource();
        Motivation motivation = new MotivationServiceImpl().getMotivationById(Integer.parseInt(vbox.getId()));
        if(motivation == null)
            return;
        selectedMotivation.setValue(motivation.getId());
        vbox.setStyle("-fx-pref-height : 300;");
    }

    private void cleanOldDataForMotivationList(){
        vBoxMotivation.getChildren().clear();
        vBoxEachMotivation.getChildren().clear();
        selectedMotivation.setValue(0);
    }

    @FXML
    private void onActionAddMotivation(){
        try {
            loadAddDialog();
        } catch (IOException e){
            e.getMessage();
        }
    }

    private void loadAddDialog() throws IOException {
        Dialog<ButtonType> addDialog = new Dialog<>();
        addDialog.initOwner(moitvationAnchorPane.getScene().getWindow());
        addDialog.setTitle("Add new motivation");
        updateBlurEffect();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getAddMotivationFXMLPath());

        addDialog.getDialogPane().setContent(fxmlLoader.load());
        addDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = addDialog.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            ControllerAddMotivationDialog controllerDialog = fxmlLoader.getController();
            Motivation motivation = controllerDialog.getData();
            if(motivation != null)
                new MotivationServiceImpl().insertMotivation(motivation);
            fillTheListOfMotivation();
        }
        updateBlurEffect();
    }

    private void updateBlurEffect(){
        if(moitvationAnchorPane.getEffect() != null)
            moitvationAnchorPane.setEffect(null);
        else
            moitvationAnchorPane.setEffect(getBlurEffect());
    }

    private Effect getBlurEffect(){
        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(55);
        adj.setInput(blur);

        return adj;
    }

    @FXML
    private void onActionDeleteMotivation(){
        new MotivationServiceImpl().deleteMotivationById(selectedMotivation.get());
        fillTheListOfMotivation();
    }

}
