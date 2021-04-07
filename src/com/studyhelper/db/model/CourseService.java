package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();
    void insertCourse(Course course);
    Course getCourseByName(String courseName);
    Course getCourseDueByName(String courseName);
}
