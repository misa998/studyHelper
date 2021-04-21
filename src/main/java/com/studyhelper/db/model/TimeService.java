package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;
import javafx.collections.ObservableList;

public interface TimeService {
    ObservableList<Time> getAllTime();
    Time getTimeByCourse_id(int course_id);
    void updateSumOfTimeForCourse(Time time);
    void insertNewTime(int course_id);
    void deleteAllTimeByCourseId(int id);
}
