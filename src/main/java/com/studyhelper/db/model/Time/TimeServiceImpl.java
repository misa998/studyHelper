package com.studyhelper.db.model.Time;

import com.studyhelper.db.entity.Time;
import com.studyhelper.db.source.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeServiceImpl implements TimeService{

    @Override
    public TimeGetListService getList() {
        return new TimeGetList();
    }

    @Override
    public TimeGetService get() {
        return new TimeGet();
    }

    @Override
    public TimeInsertService insert() {
        return new TimeInsert();
    }

    @Override
    public TimeUpdateService update() {
        return new TimeUpdate();
    }

    @Override
    public TimeDeleteService delete() {
        return new TimeDelete();
    }
}
