package com.studyhelper.db.entity;

import java.time.LocalTime;

public class Time {
    private int id;
    private LocalTime period;
    private int course_id;

    public Time(int id, LocalTime period, int course_id) {
        this.id = id;
        this.period = period;
        this.course_id = course_id;
    }
    public Time(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalTime period) {
        this.period = period;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    @Override
    public String toString() {
        return "Time{" +
                "id=" + id +
                ", period=" + period +
                ", course_id=" + course_id +
                '}';
    }
}
