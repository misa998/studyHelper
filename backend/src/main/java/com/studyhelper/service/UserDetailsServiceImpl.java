package com.studyhelper.service;

import com.studyhelper.dao.UserDetailsDAO;
import com.studyhelper.entity.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDetailsDAO userDetailsDAO;

    @Autowired
    public UserDetailsServiceImpl(@Lazy UserDetailsDAO userDetailsDAO) {
        this.userDetailsDAO = userDetailsDAO;
    }

    @Override
    public void add(UserDetails userDetails) {
        userDetailsDAO.add(userDetails);
    }

    @Override
    public UserDetails getById(BigDecimal id) {
        return userDetailsDAO.getById(id);
    }

    @Override
    public UserDetails getByUsername(String username) {
        return userDetailsDAO.getByUsername(username);
    }

}
