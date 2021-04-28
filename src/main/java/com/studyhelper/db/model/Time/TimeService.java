package com.studyhelper.db.model.Time;

public interface TimeService {
    TimeGetListService getList();
    TimeGetService get();
    TimeInsertService insert();
    TimeUpdateService update();
    TimeDeleteService delete();
}
