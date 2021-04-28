package com.studyhelper.db.model.Todo;

import com.studyhelper.db.entity.Todo;
import javafx.collections.ObservableList;

public interface TodoGetListService {

    ObservableList<Todo> all();
    ObservableList<Todo> byCourseId(int courseId);
}
