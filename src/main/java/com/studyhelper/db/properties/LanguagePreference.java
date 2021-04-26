package com.studyhelper.db.properties;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

public class LanguagePreference {
    private final Logger logger = Logger.getLogger(LanguagePreference.class.getName());

    Preferences pref = Preferences.userNodeForPackage(LanguagePreference.class);

    public LanguagePreference() {
        loadFile();
    }

    private void loadFile() {
        try (InputStream input = UiProperties.class
                .getClassLoader().getResourceAsStream("properties/pref.xml")) {
            getPref(input);

        } catch (NullPointerException | IOException | InvalidPreferencesFormatException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void getPref(InputStream input) throws IOException, InvalidPreferencesFormatException {
        Preferences.importPreferences(input);
        pref = Preferences.userNodeForPackage(LanguagePreference.class);
    }

    public void set(String value) {
        pref.put("lang", value);
    }

    public String get(String key){
        return pref.get(key, null);
    }
}
