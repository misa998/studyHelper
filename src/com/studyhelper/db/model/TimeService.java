package com.studyhelper.db.model;

import com.studyhelper.db.entity.Time;

import java.util.List;

public interface TimeService {
    public List<Time> getAll();
    Time getByCourse_id(int course_id);
    boolean updateTime(Time time);
    boolean addTime(Time time);
}
