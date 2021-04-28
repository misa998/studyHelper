package com.studyhelper.db.model.Pomodoro;

import com.studyhelper.db.properties.I18N;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PomodoroStudyStates {

    public static ObjectProperty<StudyState> studyStateProperty = new SimpleObjectProperty<>(StudyState.STOPPED);

    public enum StudyState {
        MINIPAUSE(I18N.getString("pomodoro.miniPause.enum")),
        LARGEPAUSE(I18N.getString("pomodoro.largePause.enum")),
        STUDY(I18N.getString("pomodoro.study.enum")),
        STOPPED(I18N.getString("pomodoro.stopped.enum"));

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
