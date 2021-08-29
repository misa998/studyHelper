package com.studyhelper.service;

import com.studyhelper.dao.TotalTimeSpentDAO;
import com.studyhelper.entity.time.TimePerStudy;
import com.studyhelper.entity.time.TotalTimeSpent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalTimeSpentServiceImpl implements TotalTimeSpentService{

    private TotalTimeSpentDAO totalTimeSpentDAO;

    @Autowired
    public TotalTimeSpentServiceImpl(TotalTimeSpentDAO totalTimeSpentDAO) {
        this.totalTimeSpentDAO = totalTimeSpentDAO;
    }

    public TotalTimeSpentServiceImpl() {
    }

    @Override
    public List<TotalTimeSpent> getAll() {
        return this.totalTimeSpentDAO.getAll();
    }

    @Override
    public TotalTimeSpent getById(int id) {
        return this.totalTimeSpentDAO.getById(id);
    }

    @Override
    public TotalTimeSpent getByCourseId(int id) {
        return this.totalTimeSpentDAO.getByCourseId(id);
    }

    @Override
    public void updateHours(TimePerStudy timePerStudy) {
        this.totalTimeSpentDAO.updateHours(timePerStudy);
    }

    @Override
    public void remove(int id) {
        this.totalTimeSpentDAO.remove(id);
    }

    @Override
    public void create(TotalTimeSpent totalTimeSpent) {
        this.totalTimeSpentDAO.create(totalTimeSpent);
    }
}
