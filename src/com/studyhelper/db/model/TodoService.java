package com.studyhelper.db.model;

import com.studyhelper.db.entity.Todo;
import javafx.collections.ObservableList;

public interface TodoService {
    ObservableList<Todo> getAllTodoByCourseId(int course_id);
    boolean insertTodo(Todo todo);
    ObservableList<Todo> getAllTodo();
    boolean updateTodo(Todo todo);
    void deleteTodo(Todo todo);
}
