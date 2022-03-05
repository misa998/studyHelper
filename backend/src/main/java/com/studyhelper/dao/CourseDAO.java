package com.studyhelper.dao;

import com.studyhelper.entity.Course;

import java.math.BigDecimal;
import java.util.List;

public interface CourseDAO {
    List<Course> getAll();

    void add(Course course);

    Course getById(BigDecimal id);

    Course getByName(String name);

    void remove(BigDecimal id);
}
