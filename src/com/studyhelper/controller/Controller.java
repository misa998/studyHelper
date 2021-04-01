package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.model.CourseServiceImpl;
import com.studyhelper.db.model.TimePerDayServiceImpl;
import com.studyhelper.db.source.DataSource;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
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
    private int MAX_DAYS_TO_SHOW = 31;

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
                        hours += tpd.getHours().getHour();
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

    @FXML
    public void pomodoroTabChange() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void incrementTime() {
        time = time.plusSeconds(1);
        timerLabel.setText(time.format(dtf));
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

        trayIcon.displayMessage("hey", "faggot", TrayIcon.MessageType.WARNING);
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

    @FXML
    private void startTimer(ActionEvent event) {
        if(trayIcon != null)
            trayIcon.displayMessage("Your study helper", "studying session started", TrayIcon.MessageType.INFO);

        if (timeline.getStatus().equals(Animation.Status.PAUSED)) {
            timeline.play();
            pauseTimerBtn.setDisable(false);
            startTimerBtn.setDisable(true);
        } else {
            timeline.play();
            startTimerBtn.setDisable(true);
            pauseTimerBtn.setDisable(false);
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
        pauseTimerBtn.setDisable(false);
        startTimerBtn.setDisable(false);
        timeline.stop();
        time = LocalTime.parse("00:00:00");
        timerLabel.setText(time.format(dtf));
    }
}
