package com.studyhelper.service;

import com.studyhelper.entity.Course;

import java.math.BigDecimal;
import java.util.List;

public interface CourseService {
    List<Course> getAll();

    void add(Course course);

    Course getById(BigDecimal id);

    Course getByName(String name);

    void remove(BigDecimal id);
}
