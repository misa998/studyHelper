package com.studyhelper.db.properties;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class LanguagePreference {
    private final Logger logger = Logger.getLogger(LanguagePreference.class.getName());

    Preferences pref = Preferences.userNodeForPackage(LanguagePreference.class);

    public LanguagePreference() {
    }

    public void set(String value) {
        pref.put("lang", value);
    }

    public String get(String key){
        return pref.get(key, null);
    }
}
