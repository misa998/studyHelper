package com.studyhelper.controller.Dashboard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Slider;

public class DashboardController {

    @FXML
    private Slider daysSliderForChart;
    @FXML
    private StackedBarChart<String, Double> stackedBarChart;
    @FXML
    private PieChart coursePieChart;

    GetDataForStackedChart chartData = new GetDataForStackedChart();


    public void initialize(){
        setupSliderListener();
        refreshStackedBarChart();
        pieChartSetup();
    }

    private void setupSliderListener() {
        daysSliderForChart.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                refreshStackedBarChart();
            }
        });
    }

    private void refreshStackedBarChart(){
        stackedBarChart.getData().clear();
        chartData.setDaysToShow((int) daysSliderForChart.getValue());

        stackedBarChart.getData().addAll(chartData.getAllSeries());
    }

    private void pieChartSetup() {
        coursePieChart.getData().clear();
        coursePieChart.getData().addAll(new GetDataForPieChart().get());
    }
}
