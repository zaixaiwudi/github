package com.atguigu.serviceedu.controller;


import com.atguigu.Result;
import com.atguigu.serviceedu.entity.CourseInfoVo;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.frontvo.CourseInfoFrontVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQueryVo;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.vo.CourseInfoOrders;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/serviceedu/course")
//@CrossOrigin
public class EduCourseController {
@Autowired
private EduCourseService courseService;
    //添加课程的基本信息
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return Result.ok().data("courseId",courseId);

    }

    //根据kechid查询kec信息
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfoData(courseId);
        return Result.ok().data("courseInfo",courseInfoVo);
    }

    //修改课程信息接口
    @PostMapping("updateCourse")
    public Result updateCourse(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return Result.ok();

    }

    //根据课程id查询所有信息
    @GetMapping("getConfirmcourseInfo/{courseId}")
    public Result getConfirmcourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getCoursePubilcInfoData(courseId);
        return Result.ok().data("coursePublishVo",coursePublishVo);
    }

    //最终发布
    @PutMapping("publishCourse/{courseId}")
    public Result publishCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("NorMal");
        courseService.updateById(eduCourse);
        return Result.ok();
    }


    //TODO  条件查询带分页
    @PostMapping("findAllCourse/{current}/{limit}")
    public Result findAllCourse(@RequestBody(required = false) CourseQueryVo courseQueryVo, @PathVariable long current, @PathVariable long limit){
        Page<EduCourse> pageParam = new Page<>(current,limit);
        courseService.pageQuery(courseQueryVo,pageParam);
        long total = pageParam.getTotal();
        List<EduCourse> records = pageParam.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return Result.ok();
    }

    @GetMapping("findCourse")
    public Result findCourse(){
        List<EduCourse> list = courseService.list(null);
        return Result.ok().data("list",list);
    }





}

