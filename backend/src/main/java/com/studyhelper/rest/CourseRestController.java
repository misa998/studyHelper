package com.studyhelper.rest;

import com.studyhelper.entity.Course;
import com.studyhelper.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
@ResponseBody
public class CourseRestController {
    private CourseService courseService;

    @Autowired
    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Course> getAll(){
        return courseService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Course getById(@PathVariable("id") int id){
        return courseService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer add(@RequestBody Course course){
        courseService.add(course);
        return new ResponseTransfer("hvala");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public void remove(@PathVariable("id") int id){
        courseService.remove(id);
    }
}
