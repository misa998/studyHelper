package com.studyhelper.db.properties;

import java.io.*;

import java.net.URL;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UiProperties {
    private String title = "";
    private String dashboardFXMLPath = "";
    private String courseOverviewFXMLPath = "";
    private String pomodoroFXMLPath = "";
    private String mainFXMLPath = "";
    private String editPaneFXMLPath = "";
    private String motivationFXMLPath = "";
    private int resolutionX = 0;
    private int resolutionY = 0;
    private String trayIconCaption = "";
    private String trayIconToolTip = "";
    private String mainIconPath = "";

    private final Logger logger = Logger.getLogger(UiProperties.class.getName());

    private final String UI_PROPERTIES_PATH = "properties/";
    private final String UI_PROPERTIES_NAME = "ui.properties";

    public UiProperties() {
        try {
            loadFile();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void loadFile() throws IOException {
        try(InputStream input = UiProperties.class.getClassLoader().getResourceAsStream(UI_PROPERTIES_PATH + UI_PROPERTIES_NAME)){
            getProperties(input);

        } catch (NullPointerException | IOException e){
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }
    }

    private void getProperties(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);

        title = properties.getProperty("title");
        dashboardFXMLPath = properties.getProperty("dashboardFXMLPath");
        courseOverviewFXMLPath = properties.getProperty("courseOverviewFXMLPath");
        pomodoroFXMLPath = properties.getProperty("pomodoroFXMLPath");
        mainFXMLPath = properties.getProperty("mainFXMLPath");
        editPaneFXMLPath = properties.getProperty("editPaneFXMLPath");
        motivationFXMLPath = properties.getProperty("motivationFXMLPath");
        resolutionX = Integer.parseInt(properties.getProperty("resolutionX"));
        resolutionY = Integer.parseInt(properties.getProperty("resolutionY"));
        trayIconCaption = properties.getProperty("trayIconCaption");
        trayIconToolTip = properties.getProperty("trayIconToolTip");
        mainIconPath = properties.getProperty("mainIconPath");

    }

    public URL getMotivationFXMLPath() {
        return getClass().getResource(motivationFXMLPath);
    }

    public String getMainIconPath() {
        return mainIconPath;
    }
    public URL getMainIconURL() {
        return getClass().getResource(mainIconPath);
    }

    public String getTitle(){
        return title;
    }

    public URL getDashboardFXMLPath(){
        return getClass().getResource(dashboardFXMLPath);
    }

    public URL getCourseOverviewFXMLPath() {
        return getClass().getResource(courseOverviewFXMLPath);
    }

    public URL getPomodoroFXMLPath() {
        return getClass().getResource(pomodoroFXMLPath);
    }

    public URL getMainFXMLPath() {
        return getClass().getResource(mainFXMLPath);
    }

    public URL getEditPaneFXMLPath() {
        return getClass().getResource(editPaneFXMLPath);
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public String getTrayIconCaption() {
        return trayIconCaption;
    }

    public String getTrayIconToolTip() {
        return trayIconToolTip;
    }


}
