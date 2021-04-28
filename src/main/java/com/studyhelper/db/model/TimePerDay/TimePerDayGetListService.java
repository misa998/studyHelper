package com.studyhelper.db.model.TimePerDay;

import com.studyhelper.db.entity.TimePerDay;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;

public interface TimePerDayGetListService {
    ObservableList<TimePerDay> all();
    ObservableList<TimePerDay> byCourseId(int courseId);
    ObservableList<TimePerDay> byDate(LocalDate localDate);
    ObservableList<TimePerDay> byCourseIdAndDate(int course_id, LocalDate dateOfStudy);
    ArrayList<TimePerDay> byCourseIdAndNumberOfDays(int courseId, int days);
}
