package com.studyhelper.controller.Dashboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

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

    private final Logger logger = Logger.getLogger(DashboardController.class.getName());

    private IntegerProperty daysToShowInChart = new SimpleIntegerProperty(5);
    private GetDataForStackedChart chartData = new GetDataForStackedChart(0);

    private String RADIO_URL = "http://tunein.com/popout/player/s288329";


    public void initialize(){
        setupSliderListener();
        refreshStackedBarChart();
        pieChartSetup();
        setupRadio();
    }

    private void refreshStackedBarChart(){
        stackedBarChart.getData().clear();
        chartData.setDaysToShow(daysToShowInChart.get());

        stackedBarChart.getData().addAll(chartData.getAllSeries());
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
        coursePieChart.getData().addAll(new GetDataForPieChart().get());
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
