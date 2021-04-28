package com.studyhelper.db.model.Todo;

import com.studyhelper.db.entity.Todo;

public class TodoServiceImpl implements TodoService {

    @Override
    public TodoGetListService getList() {
        return new TodoGetList();
    }

    @Override
    public TodoUpdateService update() {
        return new TodoUpdate();
    }

    @Override
    public TodoInsertService insert() {
        return new TodoInsert();
    }

    @Override
    public TodoDeleteService delete() {
        return new TodoDelete();
    }
}
