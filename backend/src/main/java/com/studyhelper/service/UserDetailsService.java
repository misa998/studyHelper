package com.studyhelper.service;

import com.studyhelper.entity.UserDetails;

import java.math.BigDecimal;

public interface UserDetailsService {
    void add(UserDetails userDetails);

    UserDetails getById(BigDecimal id);

    UserDetails getByUsername(String username);
}
