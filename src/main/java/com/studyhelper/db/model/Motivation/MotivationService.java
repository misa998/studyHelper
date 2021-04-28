package com.studyhelper.db.model.Motivation;

public interface MotivationService {
    MotivationGetListService getList();
    MotivationGetService get();
    MotivationUpdateService update();
    MotivationInsertService insert();
    MotivationDeleteService delete();
}
