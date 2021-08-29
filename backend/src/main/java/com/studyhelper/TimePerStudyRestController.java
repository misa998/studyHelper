package com.studyhelper;

import com.studyhelper.entity.time.TimePerStudy;
import com.studyhelper.service.TimePerStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/timeperstudy")
public class TimePerStudyRestController {

    private TimePerStudyService timePerStudyService;

    @Autowired
    public TimePerStudyRestController(TimePerStudyService timePerStudyService) {
        this.timePerStudyService = timePerStudyService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<TimePerStudy> getAll(){
        return timePerStudyService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public TimePerStudy getById(@PathVariable("id") int id){
        return timePerStudyService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer add(@RequestBody TimePerStudy timePerStudy){
        timePerStudyService.create(timePerStudy);
        return new ResponseTransfer("hvala");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public void remove(@PathVariable("id") int id){
        timePerStudyService.remove(id);
    }
}
