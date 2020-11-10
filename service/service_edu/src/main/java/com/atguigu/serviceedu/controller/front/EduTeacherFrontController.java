package com.atguigu.serviceedu.controller.front;

import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("serviceedu/frontteacher")
//@CrossOrigin
public class EduTeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;


    @GetMapping("getPageTeacher/{current}/{limit}")
    //分页查询讲师
    public Result getPageTeacher(@PathVariable Long current,@PathVariable Long limit){

        Page<EduTeacher> pageParam = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        teacherService.page(pageParam, wrapper);
        //把分页所有数据都要返回
        List<EduTeacher> records = pageParam.getRecords();
        long currentPage = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//时候有下一页
        boolean hasPrevious = pageParam.hasPrevious();//是否有上一页

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", currentPage);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return Result.ok().data("map",map);
    }


    //讲师详情查询
    @GetMapping("getTeacherInfo/{id}")
    public Result getTeacherInfo(@PathVariable String id){
        //根据讲师id查询讲师信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //根据讲师id查询课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper();
        wrapper.eq("teacher_id", id);
        List<EduCourse> courseList = courseService.list(wrapper);
        return Result.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }

}
