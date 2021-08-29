package com.studyhelper.dao;

import com.studyhelper.entity.time.TimePerStudy;
import com.studyhelper.entity.time.TotalTimeSpent;

import java.util.List;

public interface TotalTimeSpentDAO {
    List<TotalTimeSpent> getAll();
    TotalTimeSpent getById(int id);
    TotalTimeSpent getByCourseId(int id);
    void updateHours(TimePerStudy timePerStudy);
    void remove(int id);
    void create(TotalTimeSpent totalTimeSpent);
}
