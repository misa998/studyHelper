package com.studyhelper;

import com.studyhelper.entity.Course;
import com.studyhelper.entity.time.TotalTimeSpent;
import com.studyhelper.security.WebConfig;
import com.studyhelper.service.CourseService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest(CourseRestController.class)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = WebConfig.class)
@EnableWebMvc
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Before
    void setUp() throws Exception {
        TotalTimeSpent tts = TotalTimeSpent.builder().id(1).course(null).hours(10).timePerStudyList(null).build();
        Course course = Course.builder().name("Math").description("description").due(LocalDate.now().toString()).totalTimeSpent(tts).student(null).build();

        List<Course> allCourses = Arrays.asList(course);

        given(courseService.getAll()).willReturn(allCourses);
    }

    @Test
    void getAll() throws Exception {
        /*RequestBuilder requestBuilder = get("/courses");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(" ", result.getResponse().getContentAsString());*/
        String name = "Math";
        List<Course> allCourses = courseService.getAll();

//        assertEquals(name, allCourses.get(0).getName());
        mockMvc.perform(get("/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(name)));
    }

    @Test
    void getById() throws Exception {
        RequestBuilder requestBuilder = get("/courses/1");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(1, result.getResponse().getContentAsString());
    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }
}