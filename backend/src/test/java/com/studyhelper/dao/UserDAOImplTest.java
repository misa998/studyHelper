package com.studyhelper.dao;

import com.studyhelper.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOImplTest {

    @Autowired
    private UserDAOImpl userDAO;

    @Test
    @Order(1)
    public void getAllTest(){
        List<User> users = userDAO.getAll();
        Assertions.assertNotNull(users);

        int numberOfUsers = users.size();
        Assertions.assertTrue(numberOfUsers > 0);
    }

    @Test
    @Order(2)
    public void addTest(){
        User user = new User("john", "123");
        userDAO.add(user);

        int id = userDAO.getByUsername("john").getId();
        Assertions.assertTrue(id > 0);
    }

    @Test
    @Order(3)
    public void getByIdTest(){
        String expected = "misa";
        int id = 1;
        String actual = userDAO.getById(id).getUsername();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    public void getByUsernameTest(){
        String username = "misa";
        int expected = 1;
        int actual = userDAO.getByUsername(username).getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(6)
    public void removeTest(){
        int id = userDAO.getByUsername("john").getId();
        userDAO.remove(id);
        Assertions.assertNull(userDAO.getById(id));
    }


}
