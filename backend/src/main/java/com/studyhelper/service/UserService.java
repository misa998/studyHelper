package com.studyhelper.service;

import com.studyhelper.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    void add(User user);
    User getById(int id);
    User getByUsername(String username);
    void remove(int user);

    void edit(User user);
}
