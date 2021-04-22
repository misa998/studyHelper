package com.studyhelper.db.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    Course course;

    @BeforeEach
    void setUp() {
        course = new Course(0, null, "bbb", LocalDate.now());
    }

    @Test
    void getId() {
        assertEquals(0, course.getId());
    }

    @Test
    void setId() {
        course.setId(-1);
        assertEquals(-1, course.getId());
    }

    @Test
    void getName() {
        assertNull(course.getName());
    }

    @Test
    void setName() {
        course.setName("```");
        assertEquals("```", course.getName());
    }

    @Test
    void getDescription() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void getDue() {
        assertEquals(LocalDate.now(), course.getDue());
    }

    @Test
    void setDue() {
    }

    @Test
    void testToString() {
    }
}