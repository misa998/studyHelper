package com.studyhelper.db.model.Course;

import com.studyhelper.db.entity.Course;
import javafx.collections.ObservableList;

public interface CourseGetListService {
    ObservableList<Course> all();
}
