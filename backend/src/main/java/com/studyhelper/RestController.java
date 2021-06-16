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
public class RestController {

    private UserService userService;

    @Autowired
    public RestController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public User getAllUser(@PathVariable("id") int id){
        return userService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer addUser(@RequestBody User user){
        userService.add(user);
        return new ResponseTransfer("Thank you kamagen.");
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseTransfer editUser(@RequestBody User user){
        userService.edit(user);
        return new ResponseTransfer("Thank you kamagen.");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseTransfer deleteUser(@PathVariable("id") int id){
        userService.remove(id);
        return new ResponseTransfer("Thank you kamagen.");
    }
}
