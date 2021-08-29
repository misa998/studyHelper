package com.studyhelper.dao;

import com.studyhelper.entity.time.TimePerStudy;

import java.time.LocalDateTime;
import java.util.List;

public interface TimePerStudyDAO {
    List<TimePerStudy> getAll();
    TimePerStudy getById(int id);
    List<TimePerStudy> getByCourseId(int id);
    List<TimePerStudy> getByDate(LocalDateTime date);
    void remove(int id);
    void create(TimePerStudy timePerStudy);
}
