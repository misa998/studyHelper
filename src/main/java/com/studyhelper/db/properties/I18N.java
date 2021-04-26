package com.studyhelper.db.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {
    private static ObjectProperty<Locale> localeObjectProperty =
            new SimpleObjectProperty<>(new Locale(new LanguagePreference().get("lang")));

    private static ObjectProperty<ResourceBundle> RESOURCE_BUNDLE =
            new SimpleObjectProperty<>(
                    ResourceBundle.getBundle("labelText", localeObjectProperty.get()));
    public static ArrayList<Locale> lang = new ArrayList<>();

    public static ObjectProperty<Locale> getLocaleProperty(){
        localeObjectProperty.addListener(new ChangeListener<Locale>() {
            @Override
            public void changed(ObservableValue<? extends Locale> observable,
                                Locale oldValue, Locale newValue) {
                RESOURCE_BUNDLE.setValue(
                        ResourceBundle.getBundle("labelText", newValue));
            }
        });
        return localeObjectProperty;
    }

    public static String getString(String key) {
        return RESOURCE_BUNDLE.get().getString(key);
    }

    public static ResourceBundle getResourceBundle(){
        return RESOURCE_BUNDLE.get();
    }

    public static ArrayList<Locale> getLanguages(){
        lang.add(new Locale("sr_sp"));
        lang.add(Locale.UK);
        return lang;
    }
}
