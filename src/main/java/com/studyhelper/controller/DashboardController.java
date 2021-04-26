package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import com.studyhelper.db.model.TimePerDayServiceImpl;
import com.studyhelper.db.model.TimeServiceImpl;
import com.studyhelper.db.properties.I18N;
import com.studyhelper.db.properties.LanguagePreference;
import com.studyhelper.db.properties.UiProperties;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {

    @FXML
    private Slider daysSliderForChart;
    @FXML
    private Label unableToLoadWebViewLabel;
    @FXML
    private StackedBarChart<String, Double> stackedBarChart;
    @FXML
    private WebView webView;
    @FXML
    private PieChart coursePieChart;
    @FXML
    private ChoiceBox<Locale> langChoiceBox;

    private final Logger logger = Logger.getLogger(DashboardController.class.getName());

    private IntegerProperty daysToShowInChart = new SimpleIntegerProperty(5);
    private ObjectProperty<Locale> localeProperty = new SimpleObjectProperty<>();

    private String RADIO_URL = "http://tunein.com/popout/player/s288329";

    public void initialize(){
        setupLanguage();
        setupSliderListener();
        refreshStackedBarChart();
        pieChartSetup();
        setupRadio();
    }

    private void setupLanguage() {
        setupChoiceBox();
        localeProperty.bindBidirectional(I18N.getLocaleProperty());
        langChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Locale>() {
                    @Override
                    public void changed(ObservableValue<? extends Locale> observable,
                                        Locale oldValue, Locale newValue) {
                        localeProperty.setValue(newValue);
                        saveLangChoice(newValue);
                    }
                });
    }

    private void saveLangChoice(Locale newValue){
        new LanguagePreference().set(newValue.toLanguageTag());
    }

    private void setupChoiceBox() {
        for(Locale locale : I18N.getLanguages()){
            langChoiceBox.getItems().add(locale);
        }
        langChoiceBox.setValue(localeProperty.getValue());
    }

    private void refreshStackedBarChart(){
        stackedBarChart.getData().clear();
        ObservableList<Course> courses = new CourseServiceImpl().getList().all();

        for(int i=0; i<courses.size(); i++){
            XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
            series.setName(courses.get(i).getName());

            ObservableList<TimePerDay> timePerDayObsList = new TimePerDayServiceImpl()
                    .getByCourse_id(courses.get(i).getId());

            for(int j = 0; j < daysToShowInChart.get(); j++) {
                double minutes = 0.0;
                LocalDate date = LocalDate.now().minusDays(daysToShowInChart.subtract(j).get());

                for(int k=0; k < timePerDayObsList.size(); k++){
                    if(timePerDayObsList.get(k).getDate().isEqual(date)){
                        minutes += timePerDayObsList.get(k).getDuration().toMinutes();
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(date.getDayOfMonth()), minutes));
            }

            stackedBarChart.getData().add(series);
        }
    }

    private void setupSliderListener() {
        daysToShowInChart.bindBidirectional(daysSliderForChart.valueProperty());
        daysToShowInChart.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                refreshStackedBarChart();
            }
        });
    }

    private void pieChartSetup() {
        coursePieChart.getData().clear();
        coursePieChart.getData().addAll(getCourseDataForPieChart());
    }

    private ObservableList<PieChart.Data> getCourseDataForPieChart(){
        final ObservableList<Course> data = new CourseServiceImpl().getList().all();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Course course : data){
            Time time = new TimeServiceImpl().getTimeByCourse_id(course.getId());
            long period;
            try{
                period = time.getDuration().toHours();
            } catch (NullPointerException e){
                period = 0;
            }
            pieChartData.add(new PieChart.Data(course.getName(), period));
        }

        return pieChartData;
    }

    @FXML
    private void setupRadio(){
        unableToLoadWebViewLabel.setVisible(false);

        WebEngine webEngine = webView.getEngine();
        webEngine.setOnError(onErrorWebEngine());
        webEngine.getLoadWorker().stateProperty().addListener(failedStateListener());
        webEngine.load(RADIO_URL);
    }

    private ChangeListener<Worker.State> failedStateListener() {
        return new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue ov, Worker.State oldState,
                                Worker.State newState) {
                if (newState == Worker.State.FAILED) {
                    unableToLoadWebViewLabel.setVisible(true);
                }
            }
        };
    }

    private EventHandler<WebErrorEvent> onErrorWebEngine(){
        return new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent webErrorEvent) {
                logger.log(Level.WARNING,
                        webErrorEvent.getMessage() + " \n " +
                                "Another instance of an app may be running in the background.");
            }
        };
    }
}
