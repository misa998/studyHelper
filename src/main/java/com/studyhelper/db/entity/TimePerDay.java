package com.studyhelper.db.entity;

import java.time.Duration;
import java.time.LocalDate;

public class TimePerDay {
    private int id;
    private LocalDate date;
    private Duration duration;
    private int course_id;

    public TimePerDay(LocalDate date, Duration duration, int course_id) {
        this.date = date;
        this.duration = duration;
        this.course_id = course_id;
    }
    public TimePerDay(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    @Override
    public String toString() {
        return "TimePerDay{" +
                "id=" + id +
                ", date=" + date +
                ", duration=" + duration +
                ", course_id=" + course_id +
                '}';
    }
}
