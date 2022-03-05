package com.studyhelper.dao;

import com.studyhelper.entity.User;

import java.util.List;

public interface UserDAO {
    List<User> getAll();

    void add(User user);

    User getById(int id);

    User getByUsername(String username);

    void remove(int id);
}
