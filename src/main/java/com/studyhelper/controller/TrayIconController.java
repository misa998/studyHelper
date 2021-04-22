package com.studyhelper.controller;

import com.studyhelper.db.properties.UiProperties;

import java.awt.*;

public class TrayIconController {
    public static TrayIcon trayIcon;
    static SystemTray systemTray = null;

    public void showTray(){
        if(!isSupported())
            return;

        setupTray();

        try {
            systemTray.add(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isSupported(){
        return SystemTray.isSupported();
    }

    private void setupTray() {
        trayIcon = new TrayIconConfiguration().setupTrayIcon();
        systemTray = new TrayIconConfiguration().setupSystemTray();
    }

    public void displayMessage(String message){
        if(trayIcon != null) {
            String caption = new UiProperties().getTrayIconCaption();
            trayIcon.displayMessage(caption, message, TrayIcon.MessageType.INFO);
        }
    }

    public static void closeSystemTray(){
        if(systemTray != null)
            systemTray.remove(trayIcon);
    }
}