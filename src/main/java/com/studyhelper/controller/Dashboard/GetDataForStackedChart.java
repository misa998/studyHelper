package com.studyhelper.controller.Dashboard;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.TimePerDay;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import com.studyhelper.db.model.TimePerDay.TimePerDayServiceImpl;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.util.ArrayList;

public class GetDataForStackedChart {

    private final ArrayList<XYChart.Series<String, Double>> seriesList;
    private int daysToShow;

    public GetDataForStackedChart(int daysToShow) {
        this.seriesList = new ArrayList<>();
        this.daysToShow = daysToShow;
    }

    public void setDaysToShow(int daysToShow) {
        this.daysToShow = daysToShow;
    }

    public ArrayList<? extends XYChart.Series<String, Double>> getAllSeries(){
        seriesList.clear();
        populate();
        return seriesList;
    }

    private void populate() {
        ObservableList<Course> courses = new CourseServiceImpl().getList().all();
        for(Course course : courses)
            seriesList.add(getSeriesForCourse(course.getName(), course.getId()));
    }

    public XYChart.Series<String, Double> getSeriesForCourse(String name, int courseId) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(trimName(name));

        for(int i=0; i < daysToShow; i++) {
            series.getData().add(getDataForCourse(courseId, daysToShow - i));
        }

        return series;
    }

    private XYChart.Data<String, Double> getDataForCourse(int courseId, int numOfDays) {
        LocalDate date = LocalDate.now().minusDays(numOfDays);
        double minutes = getSumOfMinutesForADay(date, courseId);

        return new XYChart.Data<>(String.valueOf(date.getDayOfMonth()), minutes);
    }

    private double getSumOfMinutesForADay(LocalDate day, int courseId) {
        ArrayList<TimePerDay> timePerDayObsList = new TimePerDayServiceImpl()
                .getList().byCourseIdAndNumberOfDays(courseId, daysToShow);

        double minutes = 0.0;
        for (TimePerDay timePerDay : timePerDayObsList)
            if (timePerDay.getDate().isEqual(day))
                minutes += timePerDay.getDuration().toMinutes();

        return minutes;
    }

    private String trimName(String name){
        int length = Math.min(name.length(), 10);
        return name.substring(0, length);
    }
}
