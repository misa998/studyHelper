package com.studyhelper.controller.Motivation;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.Motivation.MotivationServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if(oldValue)
                    new MotivationServiceImpl().update()
                            .body(bodyTextArea.getText(), selectedMotivation.get());
            }
        });
    }

    private void setupListenerForTitleUpdate() {
        titleTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if(oldValue)
                    new MotivationServiceImpl().update()
                            .title(titleTextField.getText(), selectedMotivation.get());

                fillTheListOfMotivation();
            }
        });
    }

    private void setupEventForSelectedMotivation() {
        selectedMotivation.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                if(selectedMotivation.greaterThan(0).get())
                    fillDataForSelectedMotivation();
            }
        });
    }

    private void fillDataForSelectedMotivation() {
        Motivation motivation = new MotivationServiceImpl().get().byId(selectedMotivation.get());
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

        VBoxForMotivation vbox = new VBoxForMotivation(vBoxEachMotivation);
        GetVBoxesMotivation getVBoxes =
                new GetVBoxesMotivation(new VBoxForMotivationFactory(vbox));
        hBoxMotivations.getChildren().addAll(getVBoxes.getAll());
    }

    @FXML
    private void vboxMotivationOnAction(MouseEvent mouseEvent){
        VBox vbox = (VBox) mouseEvent.getSource();
        Motivation motivation = new MotivationServiceImpl()
                .get().byId(Integer.parseInt(vbox.getId()));
        if(motivation == null)
            return;
        selectedMotivation.setValue(motivation.getId());
    }

    private void cleanOldDataForMotivationList(){
        hBoxMotivations.getChildren().clear();
        vBoxEachMotivation.getChildren().clear();
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
        GetDialogForAddingMotivation dialog = new GetDialogForAddingMotivation();
        dialog.configureDialog();
        dialog.setInitOwner(moitvationAnchorPane.getScene().getWindow());
        dialog.getInsertDialog().showingProperty().addListener(dialogShowingProperty());

        return dialog.getInsertDialog();
    }

    private ChangeListener<? super Boolean> dialogShowingProperty() {
        return (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            updateBlurEffect();
            if (oldValue)
                fillTheListOfMotivation();
        };
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
        new MotivationServiceImpl().delete().byId(selectedMotivation.get());
        selectedMotivation.setValue(0);
        fillTheListOfMotivation();
    }
}