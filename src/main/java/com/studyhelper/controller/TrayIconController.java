package com.studyhelper.controller;

import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiPreferences;

import java.awt.TrayIcon;
import java.awt.SystemTray;

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

    private boolean isTurnedOn(){
        return Boolean.parseBoolean(new UiPreferences().get("ui.notifications"));
    }

    private boolean isSupported(){
        return SystemTray.isSupported();
    }

    private void setupTray() {
        trayIcon = new TrayIconConfiguration().setupTrayIcon();
        systemTray = new TrayIconConfiguration().setupSystemTray();
    }

    public void displayMessage(String message){
        if(trayIcon != null && isTurnedOn()) {
            String caption = I18N.getString("trayIcon.caption");
            trayIcon.displayMessage(caption, message, TrayIcon.MessageType.INFO);
        }
    }

    public static void closeSystemTray(){
        if(systemTray != null)
            systemTray.remove(trayIcon);
    }
}
