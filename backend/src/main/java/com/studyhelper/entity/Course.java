package com.studyhelper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.studyhelper.entity.time.TotalTimeSpent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "due", nullable = false)
    private String due;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "total_time_spent_id")
    private TotalTimeSpent totalTimeSpent;

    @JsonBackReference
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "student_id")
    private Student student;

    public Course(String name, String description, LocalDateTime due, TotalTimeSpent totalTimeSpent, Student student) {
        this.name = name;
        this.description = description;
        this.due = due.toString();
        this.totalTimeSpent = totalTimeSpent;
        this.student = student;
    }

    public Course(int id, String name, String description, String due, TotalTimeSpent totalTimeSpent, Student student) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due = due;
        this.totalTimeSpent = totalTimeSpent;
        this.student = student;
    }
}
