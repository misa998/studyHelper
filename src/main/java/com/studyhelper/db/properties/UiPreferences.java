package com.studyhelper.db.properties;

import java.util.prefs.Preferences;

public class UiPreferences {
    Preferences pref;

    public UiPreferences() {
        pref = Preferences.userNodeForPackage(UiPreferences.class);
    }

    public void set(String key, String value) {
        pref.put(key, value);
    }

    public String get(String key){
        String value = pref.get(key, null);
        if(value == null) {
            set("ui.notifications", String.valueOf(true));
            return String.valueOf(true);
        }
        else
            return value;
    }
}