package com.studyhelper.dao;

import com.studyhelper.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager mng){
        this.entityManager = mng;
    }

    @Override
    public List<User> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("from User", User.class);

        return query.getResultList();
    }

    @Override
    public void add(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(user);
    }

    @Override
    public User getById(int id) {
        Session session = entityManager.unwrap(Session.class);
        User user = session.get(User.class, id);
        return user;
    }

    @Override
    public User getByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("from User c where c.username=:userName", User.class);
        query.setParameter("userName", username);

        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void remove(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from User where id=:userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }
}
