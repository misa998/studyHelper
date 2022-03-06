package com.studyhelper.dao;

import com.studyhelper.entity.UserDetails;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class UserDetailsDAOImpl implements UserDetailsDAO {

    private final EntityManager entityManager;

    @Autowired
    public UserDetailsDAOImpl(final EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public void add(UserDetails userDetails) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(userDetails);
    }

    @Override
    public UserDetails getById(BigDecimal id) {
        Session session = entityManager.unwrap(Session.class);
        UserDetails userDetails = session.get(UserDetails.class, id);

        return userDetails;
    }

    @Override
    public UserDetails getByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        Query<UserDetails> query = session.createQuery("from UserDetails d left join fetch d.user where d.user.username = :userName", UserDetails.class);
        query.setParameter("userName", username);
        log.debug(query.getQueryString());
        log.debug(session.getStatistics().toString());

        return query.getSingleResult();
    }
}
