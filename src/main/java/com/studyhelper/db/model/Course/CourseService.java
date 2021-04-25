package com.studyhelper.db.model.Course;

public interface CourseService {

    CourseGet get();
    CourseGetList getList();
    CourseUpdate update();
    CourseInsert insert();
    CourseDelete delete();
}
