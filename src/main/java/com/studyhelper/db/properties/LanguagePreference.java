package com.studyhelper.db.properties;

import java.util.Locale;
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
        String value =  pref.get(key, null);
        if(value == null) {
            set(Locale.UK.toString());
            return Locale.UK.toString();
        }else
            return value;
    }
}
