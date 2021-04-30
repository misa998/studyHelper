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
    private PieChart coursePieChart;

    private final Logger logger = Logger.getLogger(DashboardController.class.getName());

    private IntegerProperty daysToShowInChart = new SimpleIntegerProperty(5);
    private GetDataForStackedChart chartData = new GetDataForStackedChart(0);

    public void initialize(){
        setupSliderListener();
        refreshStackedBarChart();
        pieChartSetup();
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
}
