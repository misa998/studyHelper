package com.studyhelper.controller.Dashboard;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Time;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import com.studyhelper.db.model.Time.TimeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class GetDataForPieChart {

    private final ObservableList<PieChart.Data> pieChartData;

    public GetDataForPieChart() {
        this.pieChartData = FXCollections.observableArrayList();
    }

    public ObservableList<PieChart.Data> get() {
        populate();
        return pieChartData;
    }

    private void populate() {
        ObservableList<Course> data = new CourseServiceImpl().getList().all();
        for(Course course : data)
            pieChartData.add(getNameAndPeriod(course));
    }

    private PieChart.Data getNameAndPeriod(Course course) {
        return new PieChart.Data(getName(course), getPeriod(course));
    }

    private double getPeriod(Course course) {
        Time time = new TimeServiceImpl().get().byCourseId(course.getId());
        try{
            return time.getDuration().toHours();
        } catch (NullPointerException e){
            return 0;
        }
    }

    private String getName(Course course) {
        return trimName(course.getName());
    }

    private String trimName(String name){
        int length = Math.min(name.length(), 10);
        return name.substring(0, length);
    }
}
