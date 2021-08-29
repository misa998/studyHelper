package com.studyhelper.dao;

import com.studyhelper.entity.Course;
import com.studyhelper.entity.time.TimePerStudy;
import com.studyhelper.entity.time.TotalTimeSpent;
import com.studyhelper.service.TotalTimeSpentService;
import com.studyhelper.service.TotalTimeSpentServiceImpl;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class TotalTimeSpentDAOTest {

    @TestConfiguration
    static class TotalTimeSpentServiceContext {
        @Bean
        public TotalTimeSpentService getTotalTimeSpentService(){
            return new TotalTimeSpentServiceImpl();
        }
    }

    @Autowired
    private TotalTimeSpentService totalTimeSpentService;
    @MockBean
    private TotalTimeSpentDAO totalTimeSpentDAO;

    @Before
    public void setUp(){
        Course course = Course.builder().name("Math").description(null).due(LocalDate.now().toString()).totalTimeSpent(null).student(null).build();
        TimePerStudy timePerStudy1 = TimePerStudy.builder().id(1).startingTime(Instant.now().toString()).hours(4.0).build();
        TimePerStudy timePerStudy2 = TimePerStudy.builder().id(2).startingTime(Instant.now().plusSeconds(5).toString()).hours(6.0).build();
        List<TimePerStudy> timePerStudyList = new ArrayList<>();
        timePerStudyList.add(timePerStudy1);
        timePerStudyList.add(timePerStudy2);
        TotalTimeSpent tts = TotalTimeSpent.builder().id(1).course(course).hours(10).timePerStudyList(timePerStudyList).build();

        Mockito.when(totalTimeSpentDAO.getById(tts.getId())).thenReturn(tts);
    }

    @Test
    public void timePerStudyTest(){
        double hours = 0.0;
        TotalTimeSpent totalTimeSpent = totalTimeSpentDAO.getById(1);
        for(TimePerStudy tps : totalTimeSpent.getTimePerStudyList()){
            hours += tps.getHours();
        }

        Assertions.assertEquals(hours, totalTimeSpent.getHours());
    }
}
