package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();
    void insertCourse(Course course);
    Course getCourseByName(String courseName);
    void deleteCourseById(int id);
    Course getCourseById(int id);
    void updateCourseDescription(String description, int id);
}
