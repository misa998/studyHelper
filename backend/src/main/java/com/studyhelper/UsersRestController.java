package com.studyhelper;


import com.studyhelper.entity.User;
import com.studyhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@ResponseBody
public class UsersRestController {

    private UserService userService;


    @Autowired
    public UsersRestController(UserService userService){
        this.userService = userService;

    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers(){

        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") int id){
        return userService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer addUser(@RequestBody User user){
        System.out.println(user);
        userService.add(user);
        return new ResponseTransfer("Thank you kamagen.");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseTransfer deleteUser(@PathVariable("id") int id){
        userService.remove(id);
        return new ResponseTransfer("Thank you kamagen.");
    }
}
