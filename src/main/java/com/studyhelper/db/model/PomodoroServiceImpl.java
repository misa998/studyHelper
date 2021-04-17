package com.studyhelper.db.model;

import com.studyhelper.db.entity.Pomodoro;
import com.studyhelper.db.entity.TimePerDay;
import java.time.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PomodoroServiceImpl implements PomodoroService{

    private static final PomodoroServiceImpl instance = new PomodoroServiceImpl();
    private PomodoroServiceImpl(){}
    public static PomodoroServiceImpl getInstance(){
        return instance;
    }

    private Pomodoro pomodoro = null;
    //private int sessionsCounter = 0;
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
    public static StudyState studyState = StudyState.STOPPED;

    @Override
    public Pomodoro setup(Pomodoro pomodoro){
        this.pomodoro = pomodoro;
        return pomodoro;
    }

    public StudyState getStudyState(){
        return studyState;
    }

    public void setStudyState(StudyState studyState){
        PomodoroServiceImpl.studyState = studyState;
    }

    public boolean isStudyTimerOver(){
        return studyTime.compareTo(pomodoro.getStudySession()) == 0;
    }

    public boolean isMiniPauseTimerOver(){
        return studyTime.compareTo(pomodoro.getMiniPause()) == 0;
    }

    public boolean isLargePauseTimerOver(){
        return studyTime.compareTo(pomodoro.getLargePause()) == 0;
    }

    public void changeStateToStudy(){
        studyState = StudyState.STUDY;
    }

    public void endStudySession(){
        if(pomodoro != null)
            new TimePerDayServiceImpl().addTimePerDay(new TimePerDay(LocalDate.now(), Duration.ofHours(studyTime.getHour()).plusMinutes(studyTime.getMinute()), pomodoro.getCourse_id()));
    }

    @Override
    public boolean updateSettings() {
        return false;
    }

    private LocalTime studyTime = LocalTime.parse("00:00:00");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /*
     * code that will be execute every 1 second by timeline
     */
    public void incrementTime() {
        studyTime = studyTime.plusSeconds(1);
    }

    public String getCurrentStudyTimeString(){
        return studyTime.format(dateTimeFormatter);
    }

    public LocalTime getCurrentStudyTime(){
        return studyTime;
    }

    public void timerReset(){
        studyTime = LocalTime.parse("00:00:00");
    }

    public void startTimer() {
        if (getStudyState() == StudyState.STOPPED) {
            studySessionCounter++;
        }

        setStudyState(StudyState.STUDY);
    }

    private static int studySessionCounter = 0;

    public void studySessionCounterReset(){
        studySessionCounter = 0;
    }

    public boolean isStudySessionOver(){
        return studySessionCounter == 4;
    }

    public int getStudySessionCounter(){
        return studySessionCounter;
    }
}
