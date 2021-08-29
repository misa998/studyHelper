package com.studyhelper.dao;

import com.studyhelper.entity.Authorities;
import com.studyhelper.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;
    private BCryptPasswordEncoder passEncoder;

    @Autowired
    public UserDAOImpl(EntityManager mng, BCryptPasswordEncoder passEncoder){
        this.entityManager = mng;
        this.passEncoder = passEncoder;
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
        System.out.println(user.getPassword());
        String password = passEncoder.encode(user.getPassword());
        user.setPassword(password);
        boolean isMatch = passEncoder.matches(user.getPassword(), password);
        System.out.println(isMatch);
        user.setAuthorities(Set.of(new Authorities("ROLE_USER")));

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
        int result = query.executeUpdate();
        System.out.println(result);
    }
}
