package com.studyhelper.entity.time;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "time_per_study")
@Entity
@Builder
public class TimePerStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "hours", nullable = false)
    private double hours;

    @Column(name = "starting_time", nullable = false)
    private String startingTime;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "total_time_spent_id")
    private TotalTimeSpent totalTimeSpent;

    public TimePerStudy(double hours, LocalDateTime localDateTime, TotalTimeSpent totalTimeSpent) {
        this.hours = hours;
        this.startingTime = localDateTime.toString();
        this.totalTimeSpent = totalTimeSpent;
    }

    public TimePerStudy(int id, double hours, String startingTime, TotalTimeSpent totalTimeSpent) {
        this.id = id;
        this.hours = hours;
        this.startingTime = startingTime;
        this.totalTimeSpent = totalTimeSpent;
    }

    public TimePerStudy() {
    }
}
