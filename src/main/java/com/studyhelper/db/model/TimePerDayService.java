package com.studyhelper.db.model;

import com.studyhelper.db.entity.TimePerDay;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public interface TimePerDayService {
    ObservableList<TimePerDay> getAllTimePerDayInstances();
    ObservableList<TimePerDay> getByCourse_id(int course_id);
    ObservableList<TimePerDay> getByDate(LocalDate localDate);
    void addTimePerDay(TimePerDay tpd);
    ObservableList<TimePerDay> getTimeByDateAndCourse_id(int course_id, LocalDate localDate);
    void updateDurationForTimePerDay(TimePerDay timePerDay);
    void deleteAllTimePerDayByCourseId(int id);
}
