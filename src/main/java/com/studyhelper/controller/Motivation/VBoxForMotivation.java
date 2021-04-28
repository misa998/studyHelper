package com.studyhelper.controller.Motivation;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/*
*   this class represents VBox shown in motivation tab
*   and components that it holds.
*
*   vBoxModel is model that you can create in javafx,
*   pass it here, and in VBoxForMotivationFactory it
*   will copy it and make list of new ones.
 */
public class VBoxForMotivation {
    private VBox vBox;
    private VBox vBoxModel;
    private Label titleLabel;

    public VBoxForMotivation() {
    }

    public VBoxForMotivation(VBox vBoxModel) {
        this.vBoxModel = vBoxModel;
    }

    public VBoxForMotivation(VBox vBox, VBox vBoxModel, Label titleLabel) {
        this.vBox = vBox;
        this.vBoxModel = vBoxModel;
        this.titleLabel = titleLabel;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public VBox getvBoxModel() {
        return vBoxModel;
    }

    public void setvBoxModel(VBox vBoxModel) {
        this.vBoxModel = vBoxModel;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }
}
