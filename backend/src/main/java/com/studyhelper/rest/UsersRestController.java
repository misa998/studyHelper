package com.studyhelper.rest;


import com.studyhelper.entity.User;
import com.studyhelper.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import lombok.extern.log4j.Log4j2;

/**
 * This controller is used for all the user related rest requests.
 *
 * @author Milos
 * @since 2.0
 */
@Log4j2
@Controller
@RequestMapping("/users")
@ResponseBody
public class UsersRestController {

    private final UserService userService;

    @Autowired
    public UsersRestController(@Lazy final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() {
        log.debug("Get all method called for rest api.");
        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") final int id) {
        log.debug("Get method called for user id " + id);
        return userService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer addUser(@RequestBody final User user) {
        log.debug("Post method for add user " + user.getUsername());
        userService.add(user);
        return new ResponseTransfer("Thank you kamagen.");
    }

    /**
     * Use this method to delete the user by it's id.
     *
     * @param id value of the user's id.
     * @return Response about the deletion.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseTransfer deleteUser(@PathVariable("id") final int id) {
        log.debug("Delete method called for user id " + id);
        userService.remove(id);
        return new ResponseTransfer("Thank you kamagen.");
    }
}
