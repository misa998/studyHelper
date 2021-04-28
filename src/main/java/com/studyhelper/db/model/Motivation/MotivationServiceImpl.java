package com.studyhelper.db.model.Motivation;

public class MotivationServiceImpl implements MotivationService{

    @Override
    public MotivationGetListService getList() {
        return new MotivationGetList();
    }

    @Override
    public MotivationGetService get() {
        return new MotivationGet();
    }

    @Override
    public MotivationUpdateService update() {
        return new MotivationUpdate();
    }

    @Override
    public MotivationInsertService insert() {
        return new MotivationInsert();
    }

    @Override
    public MotivationDeleteService delete() {
        return new MotivationDelete();
    }
}
