package com.studyhelper.service;

import com.studyhelper.dao.TimePerStudyDAO;
import com.studyhelper.entity.time.TimePerStudy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimePerStudyServiceImpl implements TimePerStudyService{

    private TimePerStudyDAO timePerStudyDAO;

    @Autowired
    public TimePerStudyServiceImpl(TimePerStudyDAO timePerStudyDAO) {
        this.timePerStudyDAO = timePerStudyDAO;
    }

    @Override
    public List<TimePerStudy> getAll() {
        return this.timePerStudyDAO.getAll();
    }

    @Override
    public TimePerStudy getById(int id) {
        return this.timePerStudyDAO.getById(id);
    }

    @Override
    public List<TimePerStudy> getByCourseId(int id) {
        return this.timePerStudyDAO.getByCourseId(id);
    }

    @Override
    public List<TimePerStudy> getByDate(LocalDateTime date) {
        return this.timePerStudyDAO.getByDate(date);
    }

    @Override
    public void remove(int id) {
        this.timePerStudyDAO.remove(id);
    }

    @Override
    public void create(TimePerStudy timePerStudy) {
        this.timePerStudyDAO.create(timePerStudy);
    }
}
