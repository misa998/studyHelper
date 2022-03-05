package com.studyhelper.service;

import com.studyhelper.dao.CourseDAO;
import com.studyhelper.entity.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;

    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public List<Course> getAll() {
        return this.courseDAO.getAll();
    }

    @Override
    public void add(Course course) {
        this.courseDAO.add(course);
    }

    @Override
    public Course getById(BigDecimal id) {
        return this.courseDAO.getById(id);
    }

    @Override
    public Course getByName(String name) {
        return this.courseDAO.getByName(name);
    }

    @Override
    public void remove(BigDecimal id) {
        this.courseDAO.remove(id);
    }
}
