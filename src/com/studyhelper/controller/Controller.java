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
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import javafx.scene.web.WebView;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.MenuItem;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Controller {

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
    private Button changeChartBtn;
    @FXML
    private TextField daysToShowTextField;
    @FXML
    private WebView webView;
    @FXML
    private ToggleButton toggleBtn;
    @FXML
    private Pane editPane;
    @FXML
    private Label timerLabel;
    @FXML
    private Button startTimerBtn;
    @FXML
    private Button pauseTimerBtn;
    @FXML
    private Button endTimerBtn;
    @FXML
    private TextField studyTxt;
    @FXML
    private TextField largePauseTxt;
    @FXML
    private TextField miniPauseTxt;
    @FXML
    private ChoiceBox<String> subjectChoiceBox;
    @FXML
    private Button skipPauseBtn;
    @FXML
    private ProgressBar timerProgress;

    public void initialize(){
        showTray();
        playRadio();
    }

    @FXML
    public void courseTabChange(){
        vBox.getChildren().clear();

        DataSource.getInstance().openConnection();
        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        if(courseList == null) {
            System.out.println("Nulcina");
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

        DataSource.getInstance().closeConnection();
    }

    @FXML
    public void addCourse() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/com/studyhelper/ui/editPaneView.fxml"));
        gridPane.setMaxWidth(editPane.getMaxWidth());
        gridPane.setMaxHeight(editPane.getMaxHeight());
        editPane.getChildren().setAll(gridPane);
    }

    private int daysToShow = 5;
    private final int MAX_DAYS_TO_SHOW = 31;

    @FXML
    public void onButtonDaysToShowChange(){
        try {
            daysToShow = Integer.parseInt(daysToShowTextField.getText());
        } catch (NumberFormatException e){
            return;
        }

        if(daysToShow > MAX_DAYS_TO_SHOW) {
            daysToShow = MAX_DAYS_TO_SHOW;
            daysToShowTextField.setText(String.valueOf(MAX_DAYS_TO_SHOW));
        }

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

            int daysCounter = daysToShow;
            // for each day A course

            for(int j=0; j<daysToShow; j++) {
                double hours = 0.0;
                LocalDate localDate = LocalDate.now().minusDays(daysToShow-j);
                List<TimePerDay> timePerDayForDayAndCourseId = new TimePerDayServiceImpl().getByDateAndCourse_id(courses[i].getId(), localDate);

                // for each hour A day
                if(timePerDayForDayAndCourseId.size() > 0) {
                    for (TimePerDay tpd : timePerDayForDayAndCourseId) {
                        //hours += tpd.getHours().getHour();
                        double min = tpd.getHours().getMinute();
                        double hrs = tpd.getHours().getHour();
                        hours += hrs * 60 + min;
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minusDays(daysToShow-j).getDayOfMonth()), hours));
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
        webEngine.load("http://tunein.com/popout/player/s288329");
        //System.out.println(webEngine.getOnError());
    }

    private Timeline timeline;
    private LocalTime time = LocalTime.parse("00:00:00");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static int studySessionCounter = 0;

    @FXML
    public void pomodoroTabChange() {
        choiceBoxFill();
        setupTimeline();
    }

    private void setupTimeline(){
        timeline = new Timeline(
                                new KeyFrame(Duration.millis(1000), ae -> {

                                    incrementTime();

                                    switch (PomodoroServiceImpl.state) {
                                        case STUDY:
                                            if (PomodoroServiceImpl.getInstance().isStudySessionOver(time))
                                                if (trayIcon != null)
                                                    trayIcon.displayMessage("Your study helper", time.getMinute() + " minutes passed in study session. " + studySessionCounter + " session over.", TrayIcon.MessageType.INFO);
                                            break;
                                        case MINIPAUSE:
                                            if (PomodoroServiceImpl.getInstance().isMiniPauseOver(time))
                                                if (trayIcon != null)
                                                    trayIcon.displayMessage("Your study helper", time.getMinute() + " minutes passed in mini pause.", TrayIcon.MessageType.INFO);
                                            break;
                                        case LARGEPAUSE:
                                            if (PomodoroServiceImpl.getInstance().isLargePauseOver(time))
                                                if (trayIcon != null)
                                                    trayIcon.displayMessage("Your study helper", time.getMinute() + " minutes passed in large pause.", TrayIcon.MessageType.INFO);
                                            break;
                                    }
                                }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }


    /*
        - choice box se azurira svaki put kada se otvori tab pomodoro
        - ako se prvi put otvara, bice popunjen
        - ako se ne otvara prvi put tj. size > 0, uzece se odabrana vrednost,
            azurirace se iznova iz baze podataka, vratice se odabrana vrednost
            (ako postoji u novoj listi)
     */
    private void choiceBoxFill() {
        String choice = null;
        if(subjectChoiceBox.getItems().size() > 0) {
            choice = subjectChoiceBox.getValue();
            subjectChoiceBox.getItems().clear();
        }
        else
            subjectChoiceBox.getItems().clear();

        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        for(Course course : courseList)
            subjectChoiceBox.getItems().add(course.getName());

        if(subjectChoiceBox.getItems().contains(choice))
            subjectChoiceBox.setValue(choice);
    }

    private void incrementTime() {
        time = time.plusSeconds(1);
        timerLabel.setText(time.format(dtf));
    }

    private void setupPomodoro(){
        LocalTime studySessionTime = LocalTime.of(0, Integer.parseInt(studyTxt.getText().equals("") ? studyTxt.getPromptText() : studyTxt.getText()), 1);
        LocalTime miniPauseTime = LocalTime.of(0, Integer.parseInt(miniPauseTxt.getText().equals("") ? miniPauseTxt.getPromptText() : miniPauseTxt.getText()), 1);
        LocalTime largePauseTime = LocalTime.of(0, Integer.parseInt(largePauseTxt.getText().equals("") ? largePauseTxt.getPromptText() : largePauseTxt.getText()), 1);

        if(subjectChoiceBox.getValue() != null) {
            PomodoroServiceImpl.getInstance().setup(new Pomodoro(studySessionTime, miniPauseTime, largePauseTime, new CourseServiceImpl().getCourseByName(subjectChoiceBox.getValue()).getId()));
            PomodoroServiceImpl.getInstance().startStudySession();
        }
        else
            System.out.println("Choice box is null");
    }

    private void timerLabelCleanUp(){
        time = LocalTime.parse("00:00:00");
        timerLabel.setText(time.format(dtf));
        timerLabel.setTextFill(Paint.valueOf("#404040"));
        //timeQuantity.setValue(0);
    }

    @FXML
    private void startTimer(ActionEvent event) {
        if(trayIcon != null)
            trayIcon.displayMessage("Your study helper", "studying session started", TrayIcon.MessageType.INFO);

        if (timeline.getStatus().equals(Animation.Status.PAUSED)) {
            timeline.play();
            PomodoroServiceImpl.getInstance().setState(PomodoroServiceImpl.State.STUDY);
            pauseTimerBtn.setDisable(false);
            startTimerBtn.setDisable(true);
            endTimerBtn.setDisable(false);
        } else {
            timerLabelCleanUp();
            timerLabel.setTextFill(Paint.valueOf("#0d6300"));
            setupPomodoro();
            timeline.play();
            studySessionCounter++;
            PomodoroServiceImpl.getInstance().setState(PomodoroServiceImpl.State.STUDY);
            startTimerBtn.setDisable(true);
            pauseTimerBtn.setDisable(false);
            endTimerBtn.setDisable(false);
        }
    }

    @FXML
    private void onPauseTimer(ActionEvent event) {
        if (timeline.getStatus().equals(Animation.Status.RUNNING)) {
            timeline.pause();
            startTimerBtn.setDisable(false);
            pauseTimerBtn.setDisable(true);
        }
    }

    @FXML
    private void onEndTimer(ActionEvent event) {
        if(studySessionCounter == 4){
            if(trayIcon != null) {
                trayIcon.displayMessage("Your study helper", studySessionCounter + " study sessions over. Big pause!", TrayIcon.MessageType.INFO);
                PomodoroServiceImpl.getInstance().setState(PomodoroServiceImpl.State.LARGEPAUSE);
                PomodoroServiceImpl.getInstance().endStudySession(time);
            }
        } else {
            PomodoroServiceImpl.getInstance().setState(PomodoroServiceImpl.State.MINIPAUSE);
            PomodoroServiceImpl.getInstance().endStudySession(time);
        }

        pauseTimerBtn.setDisable(false);
        startTimerBtn.setDisable(false);
        endTimerBtn.setDisable(true);
        timeline.stop();
        timerLabelCleanUp();

        startPause();
    }

    private void startPause(){
        endTimerBtn.setDisable(true);
        skipPauseBtn.setVisible(true);
        timerLabel.setTextFill(Paint.valueOf("#993000"));
        timeline.play();
    }

    @FXML
    private void onSkipPause(){
        timerLabelCleanUp();
        timeline.stop();
        PomodoroServiceImpl.getInstance().setState(PomodoroServiceImpl.State.STOPPED);
        skipPauseBtn.setVisible(false);
    }

    static TrayIcon trayIcon;
    static SystemTray systemTray = null;

    public void showTray(){
        if(!SystemTray.isSupported())
            System.exit(0);

        try {
            trayIcon = new TrayIcon(createImage());
        } catch (MalformedURLException e){
            e.printStackTrace();
            return;
        }
        trayIcon.setToolTip("Your study helper");

        systemTray = SystemTray.getSystemTray();
        final PopupMenu menu = new PopupMenu();

        MenuItem about = new MenuItem("About");
        MenuItem exit = new MenuItem("Exit");
        menu.add(about);
        menu.addSeparator();
        menu.add(exit);

        about.addActionListener(e -> System.out.println("About clicked."));
        exit.addActionListener(e -> System.exit(0));

        trayIcon.setPopupMenu(menu);
        try {
            systemTray.add(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeTray(){
        try {
            if(systemTray != null && trayIcon != null)
                systemTray.remove(trayIcon);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Image createImage() throws MalformedURLException {
        return new ImageIcon("/com/studyhelper/ui/resources/icon.png").getImage();
    }
}
