package com.studyhelper.rest;


import com.studyhelper.entity.User;
import com.studyhelper.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/users")
@ResponseBody
public class UsersRestController {

    private UserService userService;

    @Autowired
    public UsersRestController(@Lazy UserService userService){
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers(){
        log.debug("Get all method called for rest api.");
        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") int id){
        log.debug("Get method called for user id " + id);
        return userService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer addUser(@RequestBody User user){
        log.debug("Post method for add user " + user.getUsername());
        userService.add(user);
        return new ResponseTransfer("Thank you kamagen.");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseTransfer deleteUser(@PathVariable("id") int id){
        log.debug("Delete method called for user id " + id);
        userService.remove(id);
        return new ResponseTransfer("Thank you kamagen.");
    }
}
