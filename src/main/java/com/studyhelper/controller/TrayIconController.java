package com.studyhelper.controller;

import com.studyhelper.Main;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

public class TrayIconController {
    public static TrayIcon trayIcon;
    static SystemTray systemTray = null;
    private final String trayIconCaption = "Your study helper";
    private final String trayIconToolTip = "Your study helper";
    private final String IMAGE_URL = "/ui/icons/main-icon-black-white-128.png";

    public void showTray(){
        if(!isSupported())
            return;

        trayIcon = new TrayIcon(loadAndScaleImage());
        systemTray = SystemTray.getSystemTray();
        trayIcon.setToolTip(trayIconToolTip);
        trayIcon.setPopupMenu(configurePopupMenu());
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(onActionTrayNotification());

        try {
            systemTray.add(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Image loadAndScaleImage() {
        try {
            int width = new TrayIcon(createImageForSystemTray()).getSize().width;
            return createImageForSystemTray().getScaledInstance(width, -1, Image.SCALE_SMOOTH);
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    private PopupMenu configurePopupMenu(){
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
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("About");
                        alert.setHeaderText("About page yet to be written.");
                        alert.showAndWait();
                    }
                });
            }
        };
    }

    private ActionListener exitMenuItemActionListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        };
    }

    private ActionListener hideMenuItemActionListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSystemTray();
            }
        };
    }

    public void displayMessage(String message){
        if(trayIcon != null) {
            trayIcon.displayMessage(trayIconCaption, message, TrayIcon.MessageType.INFO);
        }
    }

    private ActionListener onActionTrayNotification(){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.showMinimizedStage();
                    }
                });
            }
        };
    }

    public static void closeSystemTray(){
        if(systemTray != null)
            systemTray.remove(trayIcon);
    }

    private Image createImageForSystemTray() throws MalformedURLException {
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMAGE_URL));
    }

    private boolean isSupported(){
        return SystemTray.isSupported();
    }
}
