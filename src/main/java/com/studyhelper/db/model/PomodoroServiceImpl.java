package com.studyhelper.db.model;

import com.studyhelper.controller.TrayIconController;
import com.studyhelper.db.entity.Pomodoro;
import com.studyhelper.db.entity.TimePerDay;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.script.SimpleBindings;
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
    private PomodoroStudyStates studyStates = new PomodoroStudyStates();

    private IntegerProperty studySessionCounter = new SimpleIntegerProperty(0);
    TrayIconController trayIconController = new TrayIconController();

    @Override
    public Pomodoro setup(Pomodoro pomodoro){
        this.pomodoro = pomodoro;
        return pomodoro;
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

    public void endStudySession(){
        if(pomodoro != null)
            new TimePerDayServiceImpl().addTimePerDay(
                    new TimePerDay(LocalDate.now(),
                            Duration.ofHours(studyTime.getHour()).plusMinutes(studyTime.getMinute()),
                            pomodoro.getCourse_id())
            );
    }

    @Override
    public boolean updateSettings() {
        return false;
    }

    private LocalTime studyTime = LocalTime.parse("00:00:00");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void incrementTime() {
        studyTime = studyTime.plusSeconds(1);
        trayIconNotificationForTimePassed();
    }

    private void trayIconNotificationForTimePassed() {
        switch (PomodoroStudyStates.studyStateProperty.get()) {
            case STUDY:
                if (isStudyTimerOver())
                    trayIconController.displayMessage(getCurrentStudyTime().getMinute() + " minutes passed in study session. " + getStudySessionCounter() + " session over.");
                break;
            case MINIPAUSE:
                if (isMiniPauseTimerOver())
                    trayIconController.displayMessage(getCurrentStudyTime().getMinute() + " minutes passed in mini pause.");
                break;
            case LARGEPAUSE:
                if (isLargePauseTimerOver())
                    trayIconController.displayMessage(getCurrentStudyTime().getMinute() + " minutes passed in large pause.");
                break;
        }
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

    public void setStartState() {
        if (studyStates.getStudyState() == PomodoroStudyStates.StudyState.STOPPED) {
            studySessionCounter.setValue(studySessionCounter.add(1).getValue());
        }

        studyStates.setStudyState(PomodoroStudyStates.StudyState.STUDY);

        trayIconController.displayMessage("studying session started");
    }

    public void studySessionCounterReset(){
        studySessionCounter.setValue(0);

        trayIconController.displayMessage("Study sessions over. Big pause!");
    }

    public boolean isStudySessionOver(){
        return studySessionCounter.get() == 4;
    }

    public int getStudySessionCounter(){
        return studySessionCounter.get();
    }
    public IntegerProperty getStudySessionCounterProperty(){
        return studySessionCounter;
    }
}
