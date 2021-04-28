package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;


public class GetVBoxesForCourses {
    private VBoxForCourseFactory factory;
    private ArrayList<VBox> allVboxes;

    public GetVBoxesForCourses(VBoxForCourseFactory factory) {
        this.factory = factory;
        this.allVboxes = new ArrayList<>();
    }

    public ArrayList<VBox> getAll(){
        ObservableList<Course> courses = getCourses();
        for(Course course : courses){
            factory.createVbox(course);
            allVboxes.add(factory.getvBoxForCourse().getvBox());
        }

        return allVboxes;
    }

    private ObservableList<Course> getCourses() {
        ObservableList<Course> courses = new CourseServiceImpl().getList().all();
        courses.sort(compareCoursesByDue());
        return courses;
    }

    private Comparator<Course> compareCoursesByDue(){
        return Comparator.comparing((Course c) -> c.getDue().isAfter(LocalDate.now()))
                .thenComparing(c -> c.getDue().isBefore(LocalDate.now()))
                .thenComparing(c -> c.getDue().isBefore(LocalDate.now().plusDays(10)))
                .reversed()
                .thenComparing(c -> c.getDue())
                ;
    }
}