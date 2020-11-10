package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.CourseInfoVo;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.frontvo.CourseInfoFrontVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfoData(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getCoursePubilcInfoData(String courseId);

    void removeCourse(String courseId);


    void pageQuery(CourseQueryVo courseQueryVo, Page<EduCourse> pageParam);

    List<EduCourse> getNewCourse();


    CourseInfoFrontVo getCourseBaseInfo(String courseId);
}
