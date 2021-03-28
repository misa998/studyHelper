package sample;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimePerDay {
    private int id;
    private LocalDate date;
    private LocalTime hours;
    private int course_id;

    public TimePerDay(LocalDate date, LocalTime hours, int course_id) {
        this.date = date;
        this.hours = hours;
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

    public LocalTime getHours() {
        return hours;
    }

    public void setHours(LocalTime hours) {
        this.hours = hours;
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
                ", hours=" + hours +
                ", course_id=" + course_id +
                '}';
    }
}
