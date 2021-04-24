package com.studyhelper.controller;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.MotivationServiceImpl;
import com.studyhelper.db.properties.UiProperties;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;

import java.io.IOException;

public class MotivationController {
    @FXML
    private Button deleteSelectedMotivation;
    @FXML
    private HBox hBoxMotivations;
    @FXML
    private VBox vBoxEachMotivation;
    @FXML
    private VBox vBoxSelectedMotivation;
    @FXML
    private AnchorPane moitvationAnchorPane;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea bodyTextArea;

    private IntegerProperty selectedMotivation = new SimpleIntegerProperty(0);

    public void initialize(){
        fillTheListOfMotivation();
        setupDeleteButton();
        setupForSelectedMotivation();
        setupEventForSelectedMotivation();
        setupListenerForTitleUpdate();
        setupListenerForBodyUpdate();
    }

    private void setupListenerForBodyUpdate() {
        bodyTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    new MotivationServiceImpl().updateMotivationBody(bodyTextArea.getText(), selectedMotivation.get());
                }
            }
        });
    }

    private void setupListenerForTitleUpdate() {
        titleTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    new MotivationServiceImpl().updateMotivationTitle(titleTextField.getText(), selectedMotivation.get());
                }
            }
        });
    }

    private void setupEventForSelectedMotivation() {
        selectedMotivation.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(selectedMotivation.greaterThan(0).get())
                    fillDataForSelectedMotivation();
            }
        });
    }

    private void fillDataForSelectedMotivation() {
        Motivation motivation = new MotivationServiceImpl().getMotivationById(selectedMotivation.get());
        fillTheFields(motivation);
    }

    private void fillTheFields(Motivation motivation) {
        titleTextField.setText(motivation.getTitle());
        bodyTextArea.setText(motivation.getBody());
    }

    private void setupForSelectedMotivation() {
        vBoxSelectedMotivation.visibleProperty().bind(
                Bindings.not(selectedMotivation.lessThan(1))
        );
    }

    private void setupDeleteButton() {
        deleteSelectedMotivation.disableProperty().bind(
                selectedMotivation.lessThan(1)
        );
    }

    private void fillTheListOfMotivation() {
        cleanOldDataForMotivationList();

        ObservableList<Motivation> motivations = new MotivationServiceImpl().getAllMotivation();
        motivations.sort((c1, c2) -> Integer.compare(c2.getId(), c1.getId()));

        for (Motivation motivation : motivations) {
            VBox vbox = createVBox(motivation);

            hBoxMotivations.getChildren().add(vbox);
        }
    }

    private VBox createVBox(Motivation motivation){
        VBox vbox = setMotivationListVBox(motivation.getId());
        Label titleLabelForList = setMotivationListLabel(motivation.getTitle());

        vbox.getChildren().add(titleLabelForList);
        return vbox;
    }

    private Label setMotivationListLabel(String title) {
        Label label = new Label(title);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
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
    }

    private void cleanOldDataForMotivationList(){
        hBoxMotivations.getChildren().clear();
        vBoxEachMotivation.getChildren().clear();
        selectedMotivation.setValue(0);
    }

    @FXML
    private void onActionAddMotivation(){
        try {
            configureDialog().showAndWait();
        } catch (IOException e){
            e.getMessage();
        }
    }
    private Dialog<Motivation> configureDialog() throws IOException {
        Dialog<Motivation> addDialog = new Dialog<>();
        addDialog.showingProperty().addListener(dialogShowingProperty());
        addDialog.initOwner(moitvationAnchorPane.getScene().getWindow());
        addDialog.initModality(Modality.WINDOW_MODAL);
        addDialog.setTitle("Add new motivation");
        addDialog.getDialogPane().setContent(getLoader().load());
        addDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        closeButtonConfig(addDialog.getDialogPane().lookupButton(ButtonType.CLOSE));

        return addDialog;
    }

    private void closeButtonConfig(Node close){
        close.managedProperty().bind(close.visibleProperty());
        close.setVisible(false);
    }

    private ChangeListener<? super Boolean> dialogShowingProperty() {
        return (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            updateBlurEffect();
            if (oldValue)
                fillTheListOfMotivation();
        };
    }

    private FXMLLoader getLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getAddMotivationFXMLPath());

        return fxmlLoader;
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