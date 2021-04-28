package com.studyhelper.controller.Motivation;

import com.studyhelper.db.entity.Motivation;
import com.studyhelper.db.model.Motivation.MotivationServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Comparator;

public class GetVBoxesMotivation {

    private VBoxForMotivationFactory factory;
    private final ArrayList<VBox> allVBoxes;

    public GetVBoxesMotivation(VBoxForMotivationFactory factory){
        this.factory = factory;
        this.allVBoxes = new ArrayList<>();
    }

    public ArrayList<VBox> getAll() {
        populate();
        return allVBoxes;
    }

    private void populate() {
        ObservableList<Motivation> motivations = getMotivations();
        for(Motivation motivation : motivations)
            allVBoxes.add(factory.createVBox(motivation));
    }

    private ObservableList<Motivation> getMotivations() {
        ObservableList<Motivation> motivations = new MotivationServiceImpl().getList().all();
        motivations.sort(getComparator());
        return motivations;
    }

    private Comparator<? super Motivation> getComparator() {
        return (c1, c2) -> Integer.compare(c2.getId(), c1.getId());
    }

/*    private VBox createVBox(Motivation motivation) {
        VBox vbox = setMotivationListVBox(motivation.getId());
        Label titleLabel = setMotivationListLabel(motivation.getTitle());

        vbox.getChildren().add(titleLabel);
        return vbox;
    }*/

/*
    private VBox setMotivationListVBox(int id) {
        VBox vbox = new VBox();
        vbox.setId(String.valueOf(id));
        vbox.setStyle(vBoxToCopyFrom.getStyle());
        vbox.setEffect(vBoxToCopyFrom.getEffect());
        vbox.setPadding(vBoxToCopyFrom.getPadding());
        vbox.setPrefSize(vBoxToCopyFrom.getPrefWidth(), vBoxToCopyFrom.getPrefHeight());
        vbox.setSpacing(10);
        vbox.setAlignment(vBoxToCopyFrom.getAlignment());
        vbox.setOnMouseClicked(vBoxToCopyFrom.getOnMouseClicked());
        vbox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                vbox.setStyle(hoverStyleBefore());
            else
                vbox.setStyle(hoverStyleAfter());
        });

        return vbox;
    }
*/

/*    private String hoverStyleAfter() {
        return "-fx-background-color : #eba694;" +
                "-fx-background-radius : 20;";
    }

    private String hoverStyleBefore() {
        return "-fx-background-color : #c3887a;" +
                "-fx-background-radius : 20;";
    }

    private Label setMotivationListLabel(String title) {
        Label label = new Label(title);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }*/
}
