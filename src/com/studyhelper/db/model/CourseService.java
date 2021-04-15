package com.studyhelper.db.model;

import com.studyhelper.db.entity.Course;
import javafx.collections.ObservableList;

public interface CourseService {

    ObservableList<Course> getAllCourses();
    void insertCourse(Course course);
    Course getCourseByName(String courseName);
    void deleteCourseById(int id);
    Course getCourseById(int id);
    void updateCourseDescription(String description, int id);
    void deleteAllDataAboutCourse(int id);
    void updateCourseName(String name, int id);
}
