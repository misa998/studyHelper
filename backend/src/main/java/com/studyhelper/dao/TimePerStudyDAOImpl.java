package com.studyhelper.dao;

import com.studyhelper.entity.time.TimePerStudy;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TimePerStudyDAOImpl implements TimePerStudyDAO {

    private EntityManager entityManager;

    @Autowired
    public TimePerStudyDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TimePerStudy> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<TimePerStudy> timePerStudyQuery = session.createQuery("from TimePerStudy", TimePerStudy.class);

        return timePerStudyQuery.getResultList();
    }

    @Override
    public TimePerStudy getById(int id) {
        Session session = entityManager.unwrap(Session.class);
        TimePerStudy timePerStudy = session.get(TimePerStudy.class, id);

        return timePerStudy;
    }

    @Override
    public List<TimePerStudy> getByCourseId(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<TimePerStudy> query = session.createQuery("from TimePerStudy c where c.course_id=:courseId", TimePerStudy.class);
        query.setParameter("courseId", id);

        return query.getResultList();
    }

    @Override
    public List<TimePerStudy> getByDate(LocalDateTime dateTime) {
        Session session = entityManager.unwrap(Session.class);
        Query<TimePerStudy> query = session.createQuery("from TimePerStudy c where c.date=:dateTime", TimePerStudy.class);
        query.setParameter("dateTime", dateTime);

        return query.getResultList();
    }

    @Override
    public void remove(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from TimePerStudy c where c.id=:id", TimePerStudy.class);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    @Transactional
    public void create(TimePerStudy timePerStudy) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(timePerStudy);
        TotalTimeSpentDAO totalTimeSpentDAO = new TotalTimeSpentDAOImpl(entityManager);
        totalTimeSpentDAO.updateHours(timePerStudy);
    }
}
