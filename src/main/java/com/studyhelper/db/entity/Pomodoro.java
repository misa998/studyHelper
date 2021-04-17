package com.studyhelper.db.entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Pomodoro {
    private LocalTime studySession;
    private LocalTime miniPause;
    private LocalTime largePause;
    private int course_id = 0;

    public Pomodoro(LocalTime studySession, LocalTime miniPause, LocalTime largePause, int course_id) {
        this.studySession = studySession;
        this.miniPause = miniPause;
        this.largePause = largePause;
        this.course_id = course_id;

    }

    public Pomodoro() {
    }

    @Override
    public String toString() {
        return "Pomodoro{" +
                "studySession=" + studySession +
                ", miniPause=" + miniPause +
                ", largePause=" + largePause +
                '}';
    }

    public LocalTime getStudySession() {
        return studySession;
    }

    public void setStudySession(LocalTime studySession) {
        this.studySession = studySession;
    }

    public LocalTime getMiniPause() {
        return miniPause;
    }

    public void setMiniPause(LocalTime miniPause) {
        this.miniPause = miniPause;
    }

    public LocalTime getLargePause() {
        return largePause;
    }

    public void setLargePause(LocalTime largePause) {
        this.largePause = largePause;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
