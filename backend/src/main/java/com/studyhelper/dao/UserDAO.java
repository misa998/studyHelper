package com.studyhelper.dao;

import com.studyhelper.entity.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAll();
    void add(User user);
    User getById(int id);
    User getByUsername(String username);
    void remove(int id);
    void edit(User user);
}
