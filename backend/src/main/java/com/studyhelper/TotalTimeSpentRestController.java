package com.studyhelper;

import com.studyhelper.entity.time.TotalTimeSpent;
import com.studyhelper.service.TotalTimeSpentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/totaltimespent")
@ResponseBody
public class TotalTimeSpentRestController {

    private TotalTimeSpentService totalTimeSpentService;

    @Autowired
    public TotalTimeSpentRestController(TotalTimeSpentService totalTimeSpentService) {
        this.totalTimeSpentService = totalTimeSpentService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<TotalTimeSpent> getAll(){
        return totalTimeSpentService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public TotalTimeSpent getById(@PathVariable("id") int id){
        return totalTimeSpentService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseTransfer add(@RequestBody TotalTimeSpent totalTimeSpent){
        totalTimeSpentService.create(totalTimeSpent);
        return new ResponseTransfer("hvala");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public void remove(@PathVariable("id") int id){
        totalTimeSpentService.remove(id);
    }
}
