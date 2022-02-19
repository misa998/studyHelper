package com.studyhelper.service;

import com.studyhelper.dao.UserDAO;
import com.studyhelper.dao.UserDAOImpl;
import com.studyhelper.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(@Lazy UserDAOImpl userDAO){
        this.userDAO = userDAO;
    }

    public UserServiceImpl(){}

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public void add(User user) {
        userDAO.add(user);
    }

    @Override
    public User getById(int id) {
        return userDAO.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userDAO.getByUsername(username);
    }

    @Override
    public void remove(int user) {
        userDAO.remove(user);
    }
}
