package com.studyhelper.db.model.Course;

import com.studyhelper.db.entity.Course;

public interface CourseGetService {

    Course byId(int id);
    Course byName(String name);
}
