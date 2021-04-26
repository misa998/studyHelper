package com.studyhelper.db.model.Course;

import java.time.LocalDate;

public interface CourseUpdateService {
    void name(String name, int id);
    void description(String description, int id);
    void due(LocalDate newValue, int id);
}
