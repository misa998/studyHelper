package com.studyhelper.db.model.Course;

public class CourseServiceImpl implements CourseService {
    @Override
    public CourseGet get() {
        return new CourseGet();
    }

    @Override
    public CourseGetList getList() {
        return new CourseGetList();
    }

    @Override
    public CourseUpdate update() {
        return new CourseUpdate();
    }

    @Override
    public CourseInsert insert() {
        return new CourseInsert();
    }

    @Override
    public CourseDelete delete() {
        return new CourseDelete();
    }
}
