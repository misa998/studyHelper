package com.studyhelper.controller;

import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.LanguagePreference;
import com.studyhelper.db.properties.UiPreferences;
import com.studyhelper.db.properties.UiProperties;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Menu languageMenu;
    @FXML
    private CheckBox notificationCheckBox;

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    public void initialize(){
        setupLanguageMenu();
        setNotifCheckBox();
        loadTabsPane();
    }

    private void loadTabsPane() {
        try {
            mainBorderPane.setCenter(FXMLLoader.load(
                            new UiProperties().getResourceURL("tabsPaneFXMLPath"),
                            I18N.getResourceBundle()));
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private ObjectProperty<Locale> localeProperty = new SimpleObjectProperty<>();

    private void setupLanguageMenu() {
        localeProperty.bindBidirectional(I18N.getLocaleProperty());
        for(Locale locale : I18N.getLanguages()) {
            MenuItem item = new MenuItem(locale.toString());
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    alertUser();
                    saveLangChoice(item.getText());
                }
            });
            languageMenu.getItems().add(item);
        }
    }

    private void saveLangChoice(String langChoice){
        Locale locale = new Locale(langChoice);
        localeProperty.setValue(locale);
        new LanguagePreference().set(locale.toString());
    }

    private void alertUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(I18N.getString("menu.langChanged.title"));
        alert.setHeaderText(I18N.getString("menu.langChanged.header"));
        alert.initOwner(mainBorderPane.getScene().getWindow());
        alert.showAndWait();
    }

    @FXML
    public void onActionTurnOffNotifications(){
        new UiPreferences().set("ui.notifications", String.valueOf(notificationCheckBox.isSelected()));
    }

    private void setNotifCheckBox() {
        notificationCheckBox.setSelected(
                Boolean.parseBoolean(
                        new UiPreferences().get("ui.notifications"))
        );
    }

    @FXML
    public void onActionAboutMenuItem(){
        try {
            getDialogConfig().showAndWait();
        } catch (IOException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private Dialog<ButtonType> getDialogConfig() throws IOException {
        Dialog<ButtonType> aboutDialog = new Dialog<>();
        aboutDialog.initOwner(mainBorderPane.getScene().getWindow());
        aboutDialog.setTitle("About");
        aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        aboutDialog.getDialogPane().setContent(getFXMLLoader().load());

        return aboutDialog;
    }

    private FXMLLoader getFXMLLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getResourceURL("aboutFXMLPath"));
        return fxmlLoader;
    }

    @FXML
    private void onActionClose(){
        System.exit(-1);
    }
}