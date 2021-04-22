package com.studyhelper.db.entity;

import java.time.Duration;

public class Time {
    private int id;
    private Duration duration;
    private int course_id;

    public Time(int id, Duration duration, int course_id) {
        this.id = id;
        this.duration = duration;
        this.course_id = course_id;
    }

    public Time(){
        this.id = 0;
        this.duration = Duration.ZERO;
        this.course_id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Time{" +
                "id=" + id +
                ", duration=" + duration +
                ", course_id=" + course_id +
                '}';
    }
}
