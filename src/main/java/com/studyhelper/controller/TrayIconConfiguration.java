package com.studyhelper.controller;

import com.studyhelper.Main;
import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.UiProperties;
import javafx.application.Platform;

import java.awt.TrayIcon;
import java.awt.SystemTray;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Objects;

public class TrayIconConfiguration {

    protected TrayIcon setupTrayIcon() {
        TrayIcon trayIcon;
        trayIcon = new TrayIcon(Objects.requireNonNull(loadAndScaleImage()));
        trayIcon.setToolTip(I18N.getString("trayIcon.caption"));
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
        return Toolkit.getDefaultToolkit().getImage(new UiProperties().getResourceURL("mainImage"));
    }

    protected PopupMenu configurePopupMenu(){
        final PopupMenu popupMenu = new PopupMenu();

        MenuItem exitMenuItem = new MenuItem(I18N.getString("trayIcon.exit.menuItem"));
        MenuItem hideMenuItem = new MenuItem(I18N.getString("trayIcon.hide.menuItem"));
        popupMenu.add(exitMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(hideMenuItem);

        exitMenuItem.addActionListener(exitMenuItemActionListener());
        hideMenuItem.addActionListener(hideMenuItemActionListener());

        return popupMenu;
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
