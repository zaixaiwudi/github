package com.atguigu.serviceedu.controller.front;

import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/serviceedu/frontindex")
//@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    //查询课程和名师
    @GetMapping("getHostCourse")
    public Result getHostCourse(){
        List<EduCourse> list = courseService.getNewCourse();
        List<EduTeacher> teacherList = teacherService.getNewTeacher();
        return Result.ok().data("courseList",list).data("teacherList",teacherList);
    }



}
