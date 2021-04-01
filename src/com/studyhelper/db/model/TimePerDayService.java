package com.studyhelper.db.model;

import com.studyhelper.db.entity.TimePerDay;

import java.time.LocalDate;
import java.util.List;

public interface TimePerDayService {
    public List<TimePerDay> getAll();
    TimePerDay getByCourse_id(int course_id);
    List<TimePerDay> getByDate(LocalDate localDate);
    boolean addTimePerDay(TimePerDay tpd);
    List<TimePerDay> getByDateAndCourse_id(int course_id, LocalDate localDate);
}
