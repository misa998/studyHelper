package com.studyhelper.entity;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User john;
    private User unknown;

    @BeforeEach
    @Ignore
    public void setUp(){
//        john = new User("john", "123");
        unknown = new User();
    }

    @Test
    public void getIdTest(){
        int expected = 0;
        int actual = john.getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getIdTest2(){
        int expected = 0;
        int actual = unknown.getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void setIdTest(){
        int expected = 10;
        john.setId(10);
        int actual = john.getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUsernameTest(){
        String expected = "john";
        String actual = john.getUsername();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUsernameTest2(){
        Assertions.assertNull(unknown.getUsername());
        Assertions.assertNull(unknown.getPassword());
    }

    @Test
    public void toStringTest(){
        String expected = "User [id=0, username=john, password=123]";
        String actual = john.toString();
        Assertions.assertEquals(expected, actual);
    }

}
