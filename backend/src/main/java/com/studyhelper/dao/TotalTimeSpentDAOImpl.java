package com.studyhelper.dao;

import com.studyhelper.entity.time.TimePerStudy;
import com.studyhelper.entity.time.TotalTimeSpent;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class TotalTimeSpentDAOImpl implements TotalTimeSpentDAO{

    private EntityManager entityManager;

    @Autowired
    public TotalTimeSpentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TotalTimeSpent> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<TotalTimeSpent> query = session.createQuery("from TotalTimeSpent", TotalTimeSpent.class);

        return query.getResultList();
    }

    @Override
    public TotalTimeSpent getById(int id) {
        Session session = entityManager.unwrap(Session.class);
        TotalTimeSpent tts = session.get(TotalTimeSpent.class, id);

        return tts;
    }

    @Override
    public TotalTimeSpent getByCourseId(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<TotalTimeSpent> query = session.createQuery("from TotalTimeSpent c where c.course_id=:courseId", TotalTimeSpent.class);
        query.setParameter("courseId", id);

        return query.getSingleResult();
    }

    @Override
    public void updateHours(TimePerStudy timePerStudy) {
        Session session = entityManager.unwrap(Session.class);
        TotalTimeSpent totalTimeSpent = getById(timePerStudy.getId());
        double existingHours = totalTimeSpent.getHours();
        totalTimeSpent.setHours(existingHours + timePerStudy.getHours());
        session.saveOrUpdate(totalTimeSpent);
    }

    @Override
    public void remove(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from TotalTimeSpent where id=:ttsId");
        query.setParameter("ttsId", id);
        int result = query.executeUpdate();
    }

    @Override
    public void create(TotalTimeSpent totalTimeSpent) {
        Session session = entityManager.unwrap(Session.class);
        totalTimeSpent.setHours(0.0);

        session.saveOrUpdate(totalTimeSpent);
    }
}
