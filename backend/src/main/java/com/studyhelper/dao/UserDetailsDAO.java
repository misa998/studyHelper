package com.studyhelper.dao;

import com.studyhelper.entity.UserDetails;

import java.math.BigDecimal;

public interface UserDetailsDAO {

    void add(UserDetails userDetails);

    UserDetails getById(BigDecimal id);

    UserDetails getByUsername(String username);
}
