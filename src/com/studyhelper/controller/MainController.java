package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Pomodoro;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.model.CourseServiceImpl;
import com.studyhelper.db.model.PomodoroServiceImpl;
import com.studyhelper.db.model.TimePerDayServiceImpl;
import com.studyhelper.db.source.DataSource;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

public class MainController {

	// https://edencoding.com/scene-background/
    @FXML
    private Pane coursePanel;
    @FXML
    private Button button;
    @FXML
    private VBox vBox;
    @FXML
    private StackedBarChart<String, Double> stackedBarChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private WebView webView;
    @FXML
    private ToggleButton toggleBtn;
    @FXML
    private Pane editCoursesPane;
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
    @FXML
    private Slider daysSliderForChart;
    @FXML
    private Label studyStateLabel;

    private final TrayIconController trayIconController = new TrayIconController();
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());

    public void initialize(){
        trayIconController.showTray();
        playRadio();
    }

    @FXML
    public void courseTabChange(){
        vBox.getChildren().clear();

        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        if(courseList == null) {
            return;
        }

        int i = 1;

        for (Course course : courseList) {
            Button button2 = new Button();
            button2.setText(course.getName());
            button2.setId("button" + i++);
            button2.setPrefWidth(button.getPrefWidth());
            button2.setPrefHeight(button.getPrefHeight());
            vBox.getChildren().add(button2);
        }
    }

    @FXML
    public void addCourseButtonAction() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/com/studyhelper/ui/editPaneView.fxml"));
        gridPane.setMaxWidth(editCoursesPane.getMaxWidth());
        gridPane.setMaxHeight(editCoursesPane.getMaxHeight());
        editCoursesPane.getChildren().setAll(gridPane);
    }

    private int daysToShowInChart = 5;

    @FXML
    private void onScrollFinishedForChart(){
        daysToShowInChart = (int) Math.round(daysSliderForChart.getValue());
        dashboardTabChanged();
    }

    @FXML
    public void onButtonDaysToShowChange(){
        dashboardTabChanged();
    }

    @FXML
    public void dashboardTabChanged(){
        // clear the chart, because of tab refresh
        stackedBarChart.getData().clear();

        // get courses
        List<Course> coursesList = new CourseServiceImpl().getAllCourses();
        if(coursesList == null)
            return;
        Course[] courses = new Course[coursesList.size()];
        coursesList.toArray(courses);
        coursesList = null;

        // get timePerDay for certain day and course_id
        //int daysToShow = 5;
        // for each course
        DataSource.getInstance().openConnection();
        for(int i=0; i<courses.length; i++){
            XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
            series.setName(courses[i].getName());

            // for each day A course

            for(int j = 0; j< daysToShowInChart; j++) {
                double hours = 0.0;
                LocalDate localDate = LocalDate.now().minusDays(daysToShowInChart -j);
                List<TimePerDay> timePerDayForDayAndCourseId = new TimePerDayServiceImpl().getTimeByDateAndCourse_id(courses[i].getId(), localDate);

                // for each hour A day
                if(timePerDayForDayAndCourseId.size() > 0) {
                    for (TimePerDay tpd : timePerDayForDayAndCourseId) {
                        //hours += tpd.getHours().getHour();
                        double min = tpd.getHours().getMinute();
                        double hrs = tpd.getHours().getHour();
                        hours += hrs * 60 + min;
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minusDays(daysToShowInChart -j).getDayOfMonth()), hours));
            }
            stackedBarChart.getData().add(series);
        }

        DataSource.getInstance().closeConnection();


        /*

        DataSource.getInstance().openConnection();
        // get courses
        List<Course> coursesList = DataSource.getInstance().getAllCourses();
        Course[] courses = new Course[coursesList.size()];
        coursesList.toArray(courses);
        // get hours
        List<TimePerDay> timePerDay = DataSource.getInstance().getAllTimePerDay();
        TimePerDay[] timePerDays = new TimePerDay[timePerDay.size()];
        timePerDay.toArray(timePerDays);
        timePerDay = null;
        coursesList = null;
        DataSource.getInstance().closeConnection();

        int daysToShow = 5;
        // iteracija kroz curseve
        for(int i=0; i<courses.length; i++){
            XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
            series.setName(courses[i].getName());

            // iteracija kroz tabelu, trazeci konkretan dan
            int daysToSearch = daysToShow;
            int daysToAdd = daysToShow;
            double hours = 0.0;
            for(int k=0; k<timePerDays.length; k++){

                for(int j=0; j<timePerDays.length; j++){
                    LocalDate day = LocalDate.now().minusDays(daysToSearch);
                    if(timePerDays[j].getDate().equals(day)) {
                        if (timePerDays[j].getCourse_id() == courses[i].getId()) {
                            System.out.println("###" + timePerDays[j] + "###" + courses[i]);
                            hours += timePerDays[j].getHours().getHour();
                        } else{
                            System.out.println(timePerDays[j] + " / " + courses[i]);
                        }
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minusDays(daysToAdd--).getDayOfMonth()), hours));
                daysToSearch--;
            }

            stackedBarChart.getData().add(series);
            //daysToShow--;
        }
        */



        /*
            1) broj predmeta
            2) for petlja
            3) new XYChart.Series
            4) iteracija i <= brojPredmeta
            5) .setName(course.name)
            - iteracija za dane unazad?
            6) izvuci podatke za prethodna pet dana
            7) uzeti sate
            8) .add(series)
         */

        /*
        // prepare
        XYChart.Series<String, Double> seriesMath = new XYChart.Series<String, Double>();
        // set displayable name
        seriesMath.setName("Math");
        // add data
        seriesMath.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().minusDays(1).getDayOfMonth()), 5.0));
        seriesMath.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().getDayOfMonth()), 2.0));

        stackedBarChart.getData().add(seriesMath);

        // prepare
        XYChart.Series<String, Double> seriesArt = new XYChart.Series<String, Double>();
        // set displayable name
        seriesArt.setName("Art");
        // add data
        seriesArt.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().minusDays(1).getDayOfMonth()), 1.5));
        seriesArt.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().getDayOfMonth()), 2.5));

        stackedBarChart.getData().add(seriesArt);
        */
    }

    @FXML
    public void liveStreamToggle(){
        if (toggleBtn.isPressed()) {
            webView.setDisable(false);
            //playRadio();
        } else {
            webView.setDisable(true);
        }
    }

    private String RADIO_URL = "http://tunein.com/popout/player/s288329";

    private void playRadio() {
        WebEngine webEngine = webView.getEngine();

        webEngine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            //stage.setTitle(webEngine.getLocation());
                        }
                    }
                });
        webEngine.load(RADIO_URL);
        //webEngine.getOnError().handle(new WebErrorEvent());
        //webEngine.load("http://tun.in/s288329");
        //System.out.println(webEngine.getOnError());
    }



    @FXML
    public void pomodoroTabChange() {
        choiceBoxFillWithCourses();
        // creating new instances on every tab change results in multiple animations that can not be stopped
        if(timelineForStudyTime == null)
            setupTimelineForStudying();
        progressBarSetup();
        //studyStateLabelSetup();
    }

    private ObjectProperty<PomodoroServiceImpl.StudyState> studyStateObjectProperty = new SimpleObjectProperty<>();
    private void studyStateLabelSetup() {
        studyStateObjectProperty.set(PomodoroServiceImpl.getInstance().getStudyState());
        studyStateLabel.textProperty().bind(studyStateObjectProperty.asString());
    }

    private void progressBarSetup() {
        DoubleProperty studySessions = new SimpleDoubleProperty();
        timerProgress.progressProperty().bind(studySessions);
        studySessions.set((PomodoroServiceImpl.getInstance().getStudySessionCounter())/4.0);
    }

    private void buttonManipulator() {
        // text fields moraju biti popunjena za start button
/*

        startTimerBtn.disableProperty().bind(
                Bindings.isEmpty(studyTxt.textProperty())
                .or(Bindings.isEmpty(miniPauseTxt.textProperty()))
                .or(Bindings.isEmpty(largePauseTxt.textProperty()))
                .or(Bindings.createBooleanBinding(() -> !endTimerBtn.isDisabled()))
                //.or(Bindings.createBooleanBinding(() -> (subjectChoiceBox.getValue() == null)))
        );
*/
        /*pauseTimerBtn.disableProperty().bind(
                Bindings.createBooleanBinding(() -> !startTimerBtn.isDisabled())
        );
        endTimerBtn.disableProperty().bind(
                Bindings.createBooleanBinding(() -> !startTimerBtn.isDisabled())
        );*/
    }

    /*
    * steps to run pomodoro
    * 1. setup timeline (increment(timer+1), isTimeOver?)
    * 2. start (labelReset(timerReset, label=currentTime, color), checkFields, disable buttons,
    * notify, sessionCounter++, timeline.play, state.Study, setup
    * 3. setup (getDataFromFields, new Pomodoro instance)
    * 4. pause (timeline.pause, buttons disable)
    * 5. end (isSessionOver ? (largePause : miniPause), addTimePerDay, disable buttons, timeline.stop,
    * labelClear, startPausetimer
     */

    private Timeline timelineForStudyTime = null;

    private void setupTimelineForStudying(){
        timelineForStudyTime = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            incrementTimeForStudyTimeline();
            trayIconNotificationForTimePassed();
        }));
        timelineForStudyTime.setCycleCount(Animation.INDEFINITE);
    }

    private void incrementTimeForStudyTimeline() {
        PomodoroServiceImpl.getInstance().incrementTime();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
    }

    /*
     * Getting all the data for new pomodoro instance
     */
    private void setupPomodoro(){
        LocalTime studySessionTime = LocalTime.of(0, Integer.parseInt(studySessionTimeTextField.getText()), 1);
        LocalTime miniPauseTime = LocalTime.of(0, Integer.parseInt(miniPauseTimeTextField.getText()), 1);
        LocalTime largePauseTime = LocalTime.of(0, Integer.parseInt(largePauseTimeTextField.getText()), 1);

        PomodoroServiceImpl.getInstance().setup(new Pomodoro(studySessionTime, miniPauseTime, largePauseTime, new CourseServiceImpl().getCourseByName(courseChoiceBox.getValue()).getId()));
    }

    public void trayIconNotificationForTimePassed() {
        switch (PomodoroServiceImpl.studyState) {
            case STUDY:
                if (PomodoroServiceImpl.getInstance().isStudyTimerOver())
                    if (TrayIconController.trayIcon != null)
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in study session. " + PomodoroServiceImpl.getInstance().getStudySessionCounter() + " session over.");
                break;
            case MINIPAUSE:
                if (PomodoroServiceImpl.getInstance().isMiniPauseTimerOver())
                    if (TrayIconController.trayIcon != null)
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in mini pause.");
                break;
            case LARGEPAUSE:
                if (PomodoroServiceImpl.getInstance().isLargePauseTimerOver())
                    if (TrayIconController.trayIcon != null)
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in large pause.");
                break;
        }
    }

    /*
        - choice box se azurira svaki put kada se otvori tab pomodoro
        - ako se prvi put otvara, bice popunjen
        - ako se ne otvara prvi put tj. size > 0, uzece se odabrana vrednost,
            azurirace se iznova iz baze podataka, vratice se odabrana vrednost
            (ako postoji u novoj listi)
     */
    private void choiceBoxFillWithCourses() {
        String choice = null;
        if(courseChoiceBox.getItems().size() > 0) {
            choice = courseChoiceBox.getValue();
            courseChoiceBox.getItems().clear();
        }
        else
            courseChoiceBox.getItems().clear();

        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        for(Course course : courseList)
            courseChoiceBox.getItems().add(course.getName());

        if(courseChoiceBox.getItems().contains(choice))
            courseChoiceBox.setValue(choice);
    }

    private void timerLabelCleanUp(){
        PomodoroServiceImpl.getInstance().timerReset();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
        studyTimerLabel.setTextFill(Paint.valueOf("#404040"));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        //progressBarSetup();
        //studyStateLabelSetup();
        if(courseChoiceBox.getValue() == null)
            return;
        if(studySessionTimeTextField.getText().equals("") || miniPauseTimeTextField.getText().equals("") || largePauseTimeTextField.getText().equals(""))
                return;

        startTimerBtn.setDisable(true);
        pauseTimerBtn.setDisable(false);
        endTimerBtn.setDisable(false);

        if(TrayIconController.trayIcon != null)
            trayIconController.displayMessage("studying session started");

        if(timelineForStudyTime.getStatus().equals(Animation.Status.STOPPED)) {
            timerLabelCleanUp();
            studyTimerLabel.setTextFill(Paint.valueOf("#0d6300"));
            setupPomodoro();
            PomodoroServiceImpl.getInstance().startTimer();
        } else{
            System.out.println("Animation status: stopped - Study resume - label not clear, timer not started, pomodoro not set");
        }

        timelineForStudyTime.play();
    }

    @FXML
    private void onPauseTimer(ActionEvent event) {
        if (timelineForStudyTime.getStatus().equals(Animation.Status.RUNNING)) {
            timelineForStudyTime.pause();
            pauseTimerBtn.setDisable(true);
            startTimerBtn.setDisable(false);
        } else if(timelineForStudyTime.getStatus().equals(Animation.Status.PAUSED)){
            System.out.println("Animation status: running");
            timelineForStudyTime.pause();
            pauseTimerBtn.setDisable(true);
            startTimerBtn.setDisable(false);
        } else if(timelineForStudyTime.getStatus().equals(Animation.Status.STOPPED)){
            System.out.println("Animation status: stopped");
            timelineForStudyTime.pause();
            pauseTimerBtn.setDisable(true);
            startTimerBtn.setDisable(false);
        }
    }

    @FXML
    private void onEndTimer(ActionEvent event) {
        if(PomodoroServiceImpl.getInstance().isStudySessionOver()){
            PomodoroServiceImpl.getInstance().studySessionCounterReset();
            if(TrayIconController.trayIcon != null) {
                trayIconController.displayMessage("Study sessions over. Big pause!");
            }
            PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.LARGEPAUSE);
        } else {
            PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.MINIPAUSE);
        }

        PomodoroServiceImpl.getInstance().endStudySession();

        pauseTimerBtn.setDisable(false);
        endTimerBtn.setDisable(true);
        startTimerBtn.setDisable(false);

        timelineForStudyTime.stop();
        timerLabelCleanUp();

        startPauseTimer();
    }

    private void startPauseTimer(){
        endTimerBtn.setDisable(true);
        skipPauseBtn.setVisible(true);
        studyTimerLabel.setTextFill(Paint.valueOf("#993000"));
        timelineForStudyTime.play();
    }

    @FXML
    private void onSkipPauseButtonAction(){
        timerLabelCleanUp();
        timelineForStudyTime.stop();
        PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.STOPPED);
        skipPauseBtn.setVisible(false);
    }
}