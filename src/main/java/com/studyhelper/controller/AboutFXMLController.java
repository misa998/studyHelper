package com.studyhelper.controller;

import com.studyhelper.db.properties.UiProperties;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class AboutFXMLController extends Application {

    @FXML
    private Hyperlink githubLink;
    @FXML
    private Hyperlink linkedInLink;
    @FXML
    private TextArea aboutTextArea;

    public void initialize(){
        setupGitHubLink();
        setupLinkedInLink();
        setupTextArea();
    }

    private void setupGitHubLink() {
        githubLink.setText("My github profile");
        githubLink.setTooltip(new Tooltip(new UiProperties().getGitHubProfileURL()));
    }

    @FXML
    public void onActionHyperLinkClick(){
        getHostServices().showDocument(githubLink.getTooltip().getText());
    }

    private void setupTextArea() {
        aboutTextArea.setText(new UiProperties().getAboutText());
    }

    @FXML
    public void onActionLinkedInClick(){
        getHostServices().showDocument(linkedInLink.getTooltip().getText());
    }

    private void setupLinkedInLink() {
        linkedInLink.setText("My linkedIn profile");
        linkedInLink.setTooltip(new Tooltip(new UiProperties().getLinkedInProfileURL()));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
