package com.studyhelper.rest;

import com.studyhelper.dao.UserDAO;
import com.studyhelper.entity.Authorities;
import com.studyhelper.entity.User;
import com.studyhelper.service.UserService;
import com.studyhelper.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class UserRestControllerIntegrationTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfig {
        @Bean
        public UserService getUserService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;
    @MockBean
    private UserDAO userDAO;

    @Before
    public void setUp() {
        Set<Authorities> auth = new HashSet<>();
        auth.add(new Authorities("ROLE_USER"));
        auth.add(new Authorities("ROLE_ADMIN"));

        User user = User.builder().id(1).username("john").password("123").email("j@gmail.com").enabled((short) 1).authorities(auth).student(null)
                .build();

        Mockito.when(userDAO.getByUsername(user.getUsername())).thenReturn(user);
    }

    @Test
    public void usernameTest() {
        String name = "john";
        User user = userDAO.getByUsername(name);

        Assertions.assertEquals(user.getUsername(), name);
    }

    @Test
    public void authTest() {
        Authorities auth = new Authorities("ROLE_USER");
        Set<Authorities> authGot = userDAO.getByUsername("john").getAuthorities();

        Assertions.assertTrue(authGot.stream()
                .anyMatch(aut -> aut.getAuthority().equals(auth.getAuthority())));
    }
}