package com.studyhelper.dao;

import com.studyhelper.entity.Course;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class CourseDAOImpl implements CourseDAO {
    private final EntityManager entityManager;

    @Autowired
    public CourseDAOImpl(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public List<Course> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Course> query = session.createQuery("from Course", Course.class);

        return query.getResultList();
    }

    @Override
    public void add(Course course) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(course);
    }

    @Override
    public Course getById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Course course = session.get(Course.class, id);
        return course;
    }

    @Override
    public Course getByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        Query<Course> query = session.createQuery("from Course c where c.name=:name", Course.class);
        query.setParameter("name", name);

        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void remove(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Course where id=:courseId");
        query.setParameter("courseId", id);
        int result = query.executeUpdate();
    }
}
