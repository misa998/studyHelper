package com.studyhelper.db.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TimePerDayTest {

    TimePerDay timePerDay;

    @BeforeEach
    void setUp() {
        timePerDay = new TimePerDay();
    }

    @Test
    void getId() {
        assertEquals(0, timePerDay.getId());
    }

    @Test
    void setId() {
    }

    @Test
    void getDate() {
        assertEquals(LocalDate.now(), timePerDay.getDate());
    }

    @Test
    void setDate() {
    }

    @Test
    void getDuration() {
        assertEquals(Duration.ofHours(0), timePerDay.getDuration());
    }

    @Test
    void setDuration() {
    }

    @Test
    void getCourse_id() {
    }

    @Test
    void setCourse_id() {
    }
}