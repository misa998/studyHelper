package com.studyhelper.db.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    Time time;

    @BeforeEach
    void setUp() {
        time = new Time();
    }

    @Test
    void getId() {
    }

    @Test
    void setId() {
    }

    @Test
    void getDuration() {
        assertEquals(Duration.ofSeconds(0), time.getDuration());
    }

    @Test
    void setDuration() {
        Time oldTimeValue = new Time(1, Duration.ofMinutes(25), 1);
        Time timeToAdd = new Time(1, Duration.ofMinutes(75), 1);
        Duration newTimeValue = oldTimeValue.getDuration()
                //.plusHours(timeToAdd.getDuration().toHoursPart())
//                .plusMinutes(timeToAdd.getDuration().toMinutesPart())
                .plusMinutes(timeToAdd.getDuration().toMinutes())
        ;

        System.out.println(oldTimeValue);
        System.out.println(timeToAdd);
        System.out.println(newTimeValue);

        assertTrue(oldTimeValue.getDuration().toMinutes() < newTimeValue.toMinutes());
        assertEquals(100.0, newTimeValue.toMinutes());
    }

    @Test
    void getCourse_id() {
    }

    @Test
    void setCourse_id() {
    }
}