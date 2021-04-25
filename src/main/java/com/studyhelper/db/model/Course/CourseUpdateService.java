package com.studyhelper.db.model.Course;

public interface CourseUpdateService {
    void name(String name, int id);
    void description(String description, int id);
}
