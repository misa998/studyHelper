package com.studyhelper.db.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PomodoroStudyStates {


    public static ObjectProperty<StudyState> studyStateProperty = new SimpleObjectProperty<>(StudyState.STOPPED);

    public enum StudyState {
        MINIPAUSE("On mini pause"),
        LARGEPAUSE("On large pause"),
        STUDY("Studying"),
        STOPPED("Not studying");

        private String description;

        private StudyState(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public ObjectProperty<StudyState> getStudyStateProperty(){
        return studyStateProperty;
    }

    public StudyState getStudyState(){
        return studyStateProperty.get();
    }

    public void setStudyState(StudyState studyState){
        studyStateProperty.setValue(studyState);
    }
}
