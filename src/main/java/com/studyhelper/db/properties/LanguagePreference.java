package com.studyhelper.db.properties;

import java.util.prefs.Preferences;

public class LanguagePreference {
    Preferences pref;

    public LanguagePreference() {
        pref = Preferences.userNodeForPackage(LanguagePreference.class);
    }

    public void set(String value) {
        pref.put("lang", value);
    }

    public String get(String key){
        return pref.get(key, null);
    }
}
