package com.studyhelper.controller.Motivation;

import com.studyhelper.db.entity.Motivation;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/*
*   this class holds the logic behind vboxes
*   and creates them.
*
*   Give VBoxForMotivation with model to the constructor.
 */
public class VBoxForMotivationFactory {
    private VBoxForMotivation vBoxForMotivation;

    public VBoxForMotivationFactory(VBoxForMotivation vBoxForMotivation) {
        this.vBoxForMotivation = vBoxForMotivation;
    }

    public VBoxForMotivation getvBoxForMotivation() {
        return vBoxForMotivation;
    }

    public VBox createVBox(Motivation motivation){
        VBox vbox = setMotivationListVBox(motivation.getId());
        Label titleLabel = setMotivationListLabel(motivation.getTitle());

        vbox.getChildren().add(titleLabel);
        return vbox;
    }

    private VBox setMotivationListVBox(int id) {
        VBox vbox = new VBox();
        vbox.setId(String.valueOf(id));
        vbox.setStyle(vBoxForMotivation.getvBoxModel().getStyle());
        vbox.setEffect(vBoxForMotivation.getvBoxModel().getEffect());
        vbox.setPadding(vBoxForMotivation.getvBoxModel().getPadding());
        vbox.setPrefSize(vBoxForMotivation.getvBoxModel().getPrefWidth(),
                vBoxForMotivation.getvBoxModel().getPrefHeight());
        vbox.setSpacing(10);
        vbox.setAlignment(vBoxForMotivation.getvBoxModel().getAlignment());
        vbox.setOnMouseClicked(vBoxForMotivation.getvBoxModel().getOnMouseClicked());
        vbox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                vbox.setStyle(hoverStyleBefore());
            else
                vbox.setStyle(hoverStyleAfter());
        });

        return vbox;
    }

    private String hoverStyleAfter() {
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
    }
}
