package com.studyhelper.db.model.Todo;

public interface TodoService {
    TodoGetListService getList();
    TodoUpdateService update();
    TodoInsertService insert();
    TodoDeleteService delete();
}
