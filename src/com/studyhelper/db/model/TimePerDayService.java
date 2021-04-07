package com.studyhelper.db.model;

import com.studyhelper.db.entity.TimePerDay;

import java.time.LocalDate;
import java.util.List;

public interface TimePerDayService {
    public List<TimePerDay> getAllTimePerDayInstances();
    TimePerDay getByCourse_id(int course_id);
    List<TimePerDay> getByDate(LocalDate localDate);
    boolean addTimePerDay(TimePerDay tpd);
    List<TimePerDay> getTimeByDateAndCourse_id(int course_id, LocalDate localDate);
}
