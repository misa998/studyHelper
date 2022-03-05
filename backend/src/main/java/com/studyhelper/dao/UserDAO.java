package com.studyhelper.dao;

import com.studyhelper.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDAO {
    List<User> getAll();

    void add(User user);

    User getById(BigDecimal id);

    User getByUsername(String username);

    void remove(BigDecimal id);
}
