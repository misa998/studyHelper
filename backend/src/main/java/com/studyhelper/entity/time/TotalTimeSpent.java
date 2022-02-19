package com.studyhelper.entity.time;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studyhelper.entity.Course;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "total_time_spent")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalTimeSpent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "hours", nullable = false)
    private double hours;

    @JsonManagedReference(value = "totalTimeSpent")
    @OneToOne(mappedBy = "totalTimeSpent", cascade = CascadeType.ALL)
    private Course course;

    @JsonManagedReference(value = "timePerStudyList")
    @OneToMany(mappedBy="totalTimeSpent", cascade= {CascadeType.DETACH,
            	CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,})
    private List<TimePerStudy> timePerStudyList;
}
