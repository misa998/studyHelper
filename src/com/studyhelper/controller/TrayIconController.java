package com.studyhelper.controller;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class TrayIconController {
    public static TrayIcon trayIcon;
    static SystemTray systemTray = null;
    private final String trayIconCaption = "Your study helper";
    private final String trayIconToolTip = "Your study helper";
    private final String IMAGE_URL = "/com/studyhelper/ui/resources/icon.png";

    public void showTray(){
        if(!isSupported())
            System.exit(0);

        try {
            trayIcon = new TrayIcon(createImageForSystemTray());
        } catch (MalformedURLException e){
            e.printStackTrace();
            return;
        }
        trayIcon.setToolTip(trayIconToolTip);

        systemTray = SystemTray.getSystemTray();
        final PopupMenu popupMenu = new PopupMenu();

        MenuItem aboutMenuItem = new MenuItem("About");
        MenuItem exitMenuItem = new MenuItem("Exit");
        popupMenu.add(aboutMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(exitMenuItem);

        aboutMenuItem.addActionListener(listener -> System.out.println("About clicked."));
        exitMenuItem.addActionListener(listener -> System.exit(0));

        trayIcon.setPopupMenu(popupMenu);
        try {
            systemTray.add(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayMessage(String message){
        trayIcon.displayMessage(trayIconCaption, message, TrayIcon.MessageType.INFO);
    }

    public static void closeSystemTray(){
        try {
            if(systemTray != null && trayIcon != null)
                systemTray.remove(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Image createImageForSystemTray() throws MalformedURLException {
        return new ImageIcon(IMAGE_URL).getImage();
    }

    private boolean isSupported(){
        return SystemTray.isSupported();
    }
}
