package com.studyhelper.controller;

import com.studyhelper.Main;
import com.studyhelper.db.properties.UiProperties;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Objects;

public class TrayIconConfiguration {

    protected TrayIcon setupTrayIcon() {
        TrayIcon trayIcon;
        trayIcon = new TrayIcon(Objects.requireNonNull(loadAndScaleImage()));
        trayIcon.setToolTip(new UiProperties().getTrayIconToolTip());
        trayIcon.setPopupMenu(configurePopupMenu());
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(onActionTrayNotification());

        return trayIcon;
    }

    protected SystemTray setupSystemTray() {
        return SystemTray.getSystemTray();
    }

    protected Image loadAndScaleImage() {
        try {
            int width = new TrayIcon(createImageForSystemTray()).getSize().width;
            return createImageForSystemTray().getScaledInstance(width, -1, Image.SCALE_SMOOTH);
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    private Image createImageForSystemTray() throws MalformedURLException {
        return Toolkit.getDefaultToolkit().getImage(new UiProperties().getMainIconURL());
    }

    protected PopupMenu configurePopupMenu(){
        final PopupMenu popupMenu = new PopupMenu();

        MenuItem aboutMenuItem = new MenuItem("About");
        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem hideMenuItem = new MenuItem("Hide System Tray");
        popupMenu.add(aboutMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(exitMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(hideMenuItem);

        aboutMenuItem.addActionListener(aboutMenuItemActionListener());
        exitMenuItem.addActionListener(exitMenuItemActionListener());
        hideMenuItem.addActionListener(hideMenuItemActionListener());

        return popupMenu;
    }

    private ActionListener aboutMenuItemActionListener(){
        return e -> Platform.runLater(() -> new MainController().onActionAboutMenuItem());
    }

    private ActionListener exitMenuItemActionListener(){
        return e -> System.exit(-1);
    }

    private ActionListener hideMenuItemActionListener(){
        return e -> TrayIconController.closeSystemTray();
    }

    private ActionListener onActionTrayNotification(){
        return e -> Platform.runLater(() -> Main.showMinimizedStage());
    }
}
