package com.studyhelper.db.model.Course;

public interface CourseService {

    CourseGetService get();
    CourseGetListService getList();
    CourseUpdateService update();
    CourseInsertService insert();
    CourseDeleteService delete();
}
