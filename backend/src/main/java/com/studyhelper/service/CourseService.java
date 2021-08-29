package com.studyhelper.service;

import com.studyhelper.entity.Course;

import java.util.List;

public interface CourseService {
    public List<Course> getAll();
    void add(Course course);
    Course getById(int id);
    Course getByName(String name);
    void remove(int id);
}
