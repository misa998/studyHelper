package com.studyhelper.db.model;

import com.studyhelper.db.entity.Pomodoro;
import com.studyhelper.db.entity.TimePerDay;

import java.time.LocalDate;
import java.time.LocalTime;

public class PomodoroServiceImpl implements PomodoroService{

    private static PomodoroServiceImpl instance = new PomodoroServiceImpl();
    private PomodoroServiceImpl(){}
    public static PomodoroServiceImpl getInstance(){
        return instance;
    }

    private Pomodoro pomodoro = null;
    //private int sessionsCounter = 0;
    public static enum State {
        MINIPAUSE,
        LARGEPAUSE,
        STUDY,
        STOPPED;

        private State() {
        }
    }
    public static State state = State.STOPPED;

    @Override
    public Pomodoro setup(Pomodoro pmd){
        pomodoro = pmd;
        return pomodoro;
    }

    public State getState(){
        return state;
    }

    public void setState(PomodoroServiceImpl.State state){
        PomodoroServiceImpl.state = state;
    }

    public boolean isStudySessionOver(LocalTime time){
        return time.compareTo(pomodoro.getStudySession()) == 0;
    }

    public boolean isMiniPauseOver(LocalTime time){
        return time.compareTo(pomodoro.getMiniPause()) == 0;
    }

    public boolean isLargePauseOver(LocalTime time){
        return time.compareTo(pomodoro.getLargePause()) == 0;
    }

    public void startStudySession(){
        state = State.STUDY;
    }

    public void endStudySession(LocalTime time){
        if(pomodoro != null)
            new TimePerDayServiceImpl().addTimePerDay(new TimePerDay(LocalDate.now(), time, pomodoro.getCourse_id()));
    }

    @Override
    public boolean updateSettings() {
        return false;
    }
}
