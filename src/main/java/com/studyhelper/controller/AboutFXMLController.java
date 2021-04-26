package com.studyhelper.controller;

import com.studyhelper.db.properties.I18N;
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
        githubLink.setText("Github profile");
        githubLink.setTooltip(new Tooltip("https://github.com/misa998"));
    }

    @FXML
    public void onActionHyperLinkClick(){
        getHostServices().showDocument(githubLink.getTooltip().getText());
    }

    private void setupTextArea() {
        aboutTextArea.setText(I18N.getString("about.text"));
    }

    @FXML
    public void onActionLinkedInClick(){
        getHostServices().showDocument(linkedInLink.getTooltip().getText());
    }

    private void setupLinkedInLink() {
        linkedInLink.setText("LinkedIn profile");
        linkedInLink.setTooltip(
                new Tooltip("https://www.linkedin.com/in/milos-i-9a6552a0/"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
