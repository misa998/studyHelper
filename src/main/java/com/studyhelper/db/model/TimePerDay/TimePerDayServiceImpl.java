package com.studyhelper.db.model.TimePerDay;

public class TimePerDayServiceImpl implements TimePerDayService {
    @Override
    public TimePerDayGetListService getList() {
        return new TimePerDayGetList();
    }

    @Override
    public TimePerDayInsertService insert() {
        return new TimePerDayInsert();
    }

    @Override
    public TimePerDayUpdateService update() {
        return new TimePerDayUpdate();
    }

    @Override
    public TimePerDayDeleteService delete() {
        return new TimePerDayDelete();
    }
}
