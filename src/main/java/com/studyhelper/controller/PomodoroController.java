package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Pomodoro;
import com.studyhelper.db.model.CourseServiceImpl;
import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.model.PomodoroStudyStates;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;

import java.time.LocalTime;
import java.util.Comparator;

import static com.studyhelper.db.model.PomodoroStudyStates.StudyState.*;

public class PomodoroController {

    @FXML
    private Label studyStateLabel;
    @FXML
    private Label studyTimerLabel;
    @FXML
    private Button startTimerBtn;
    @FXML
    private Button pauseTimerBtn;
    @FXML
    private Button endTimerBtn;
    @FXML
    private TextField studySessionTimeTextField;
    @FXML
    private TextField largePauseTimeTextField;
    @FXML
    private TextField miniPauseTimeTextField;
    @FXML
    private ChoiceBox<String> courseChoiceBox;
    @FXML
    private Button skipPauseBtn;
    @FXML
    private ProgressBar timerProgress;

    private DoubleProperty studySessionsProperty = new SimpleDoubleProperty();
    private ObjectProperty<PomodoroStudyStates.StudyState> studyStateObjectProperty = new SimpleObjectProperty<>();
    private Timeline timelineForStudyTime = null;


    public void initialize(){
        choiceBoxFillWithCourses();

        if(timelineForStudyTime == null)
            setupTimelineForStudying();
        studyStateLabelSetup();
        setupPomodoroTimerButtonBindings();
    }

    private void choiceBoxFillWithCourses() {
        String choice = getOldChoice();
        getCoursesForChoiceBox();
        // set old choice
        if(courseChoiceBox.getItems().contains(choice))
            courseChoiceBox.setValue(choice);
    }

    private String getOldChoice(){
        String choice = "";
        if(courseChoiceBox.getItems().size() > 0)
            choice = courseChoiceBox.getValue();

        courseChoiceBox.getItems().clear();

        return choice;
    }

    private void getCoursesForChoiceBox() {
        ObservableList<Course> courseList = new CourseServiceImpl().getAllCourses();
        courseList.sort(Comparator.comparing((Course c) -> c.getName()));
        for(Course course : courseList) courseChoiceBox.getItems().add(course.getName());
    }

    private void studyStateLabelSetup() {
        studyStateObjectProperty.bindBidirectional(PomodoroStudyStates.studyStateProperty);
        studyStateLabel.textProperty().bind(studyStateObjectProperty.asString());
    }

    private void progressBarSetup() {
        studySessionsProperty.bindBidirectional(PomodoroServiceImpl.getInstance().getStudySessionCounterProperty());
        timerProgress.progressProperty().bind(studySessionsProperty.divide(4));
    }

    private void setupPomodoroTimerButtonBindings() {
        startTimerBtn.disableProperty().bind(
                Bindings.isEmpty(studySessionTimeTextField.textProperty())
                        .or(Bindings.isEmpty(miniPauseTimeTextField.textProperty()))
                        .or(Bindings.isEmpty(largePauseTimeTextField.textProperty()))
                        .or(Bindings.isNull(courseChoiceBox.valueProperty()))
                        .or(Bindings.not(pauseTimerBtn.disabledProperty()))
                        .or(Bindings.selectBoolean(skipPauseBtn.visibleProperty()))
        );
        endTimerBtn.disableProperty().bind(
                Bindings.selectBoolean(pauseTimerBtn.disabledProperty())
        );
    }

    private void setupTimelineForStudying(){
        timelineForStudyTime = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000), actionEvent -> {
            incrementTimeForStudyTimeline();
        }));
        timelineForStudyTime.setCycleCount(Animation.INDEFINITE);
    }

    private void incrementTimeForStudyTimeline() {
        PomodoroServiceImpl.getInstance().incrementTime();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
    }

    private void setupPomodoro(){
        try {
            LocalTime studySessionTime = LocalTime.of(0, Integer.parseInt(studySessionTimeTextField.getText()), 1);
            LocalTime miniPauseTime = LocalTime.of(0, Integer.parseInt(miniPauseTimeTextField.getText()), 1);
            LocalTime largePauseTime = LocalTime.of(0, Integer.parseInt(largePauseTimeTextField.getText()), 1);

            PomodoroServiceImpl.getInstance().setup(new Pomodoro(studySessionTime, miniPauseTime, largePauseTime, new CourseServiceImpl().getCourseByName(courseChoiceBox.getValue()).getId()));
        } catch (IllegalArgumentException e){
            studySessionTimeTextField.requestFocus();
        }
    }



    private void timerLabelCleanUp(){
        PomodoroServiceImpl.getInstance().timerReset();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
        studyTimerLabel.setTextFill(Paint.valueOf("#404040"));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        pauseTimerBtn.setDisable(false);

        if(timelineForStudyTime.getStatus().equals(Animation.Status.STOPPED)) {
            timerLabelCleanUp();
            studyTimerLabel.setTextFill(Paint.valueOf("#0d6300"));
            setupPomodoro();
            PomodoroServiceImpl.getInstance().setStartState();
        }

        timelineForStudyTime.play();
        progressBarSetup();
    }

    @FXML
    private void onPauseTimer(ActionEvent event) {
        timelineForStudyTime.pause();
        pauseTimerBtn.setDisable(true);
    }

    @FXML
    private void onEndTimer(ActionEvent event) {
        if(PomodoroServiceImpl.getInstance().isStudySessionOver()){
            PomodoroServiceImpl.getInstance().studySessionCounterReset();

            PomodoroStudyStates.studyStateProperty.setValue(LARGEPAUSE);
        } else {
            PomodoroStudyStates.studyStateProperty.setValue(MINIPAUSE);
        }

        PomodoroServiceImpl.getInstance().endStudySession();

        pauseTimerBtn.setDisable(true);

        timelineForStudyTime.stop();
        timerLabelCleanUp();

        startPauseTimer();
    }

    private void startPauseTimer(){
        skipPauseBtn.setVisible(true);
        studyTimerLabel.setTextFill(Paint.valueOf("#993000"));
        timelineForStudyTime.play();
    }

    @FXML
    private void onSkipPauseButtonAction(){
        timerLabelCleanUp();
        timelineForStudyTime.stop();
        PomodoroStudyStates.studyStateProperty.setValue(PomodoroStudyStates.StudyState.STOPPED);
        skipPauseBtn.setVisible(false);
    }
}
