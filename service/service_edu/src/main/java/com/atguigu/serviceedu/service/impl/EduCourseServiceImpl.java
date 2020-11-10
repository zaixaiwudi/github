package com.atguigu.serviceedu.service.impl;

import com.atguigu.Result;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.CourseInfoVo;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduCourseDescription;
import com.atguigu.serviceedu.entity.frontvo.CourseInfoFrontVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQueryVo;
import com.atguigu.serviceedu.mapper.EduCourseMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseDescriptionService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

/*    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //这是添加课程
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert<=0){
            throw new GuliException(20001,"添加失败");
        }
        String courseId = eduCourse.getId();
        //添加描述
        EduCourseDescription courseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, courseDescription);
        courseDescription.setId(courseId);
        //把描述信息放到service中
        courseDescriptionService.save(courseDescription);
        return courseId;
        }*/
    //注入描述service
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //添加课程信息到课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert<=0){  //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }
        //获取课程的id
        String courseId = eduCourse.getId();
        //添加描述信息到描述表中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        //把id放到描述表中当描述表的id，因为是一对一的关系
        eduCourseDescription.setId(courseId);
        //获取到描述信息
        String description = courseInfoVo.getDescription();
        //把描述信息放到描述类中
        eduCourseDescription.setDescription(description);
        courseDescriptionService.save(eduCourseDescription);
        return courseId;
    }

    //根据id查询课程信息
    @Override
    public CourseInfoVo getCourseInfoData(String courseId) {
        //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        //再根据id查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(courseDescription, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update<=0){
            throw new GuliException(20001,"修改失败");
        }

        EduCourseDescription courseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, courseDescription);

        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo getCoursePubilcInfoData(String id) {

        return baseMapper.getCoursePublicInfo(id);
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //先删除小节
        videoService.deleteVideoByCourseId(courseId);
        //再删除章节
        chapterService.deleteChapterByCourseId(courseId);
        //删除描述
        courseDescriptionService.removeById(courseId);
        //删除课程本身
        int delete = baseMapper.deleteById(courseId);
        if (delete <=0){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    //条件查询加分页
    @Override
    public void pageQuery(CourseQueryVo courseQueryVo, Page<EduCourse> pageParam) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper();
        String status = courseQueryVo.getStatus();
        System.out.println("status = " + status);
        String title = courseQueryVo.getTitle();
        System.out.println("title = " + title);

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.like("status", status);

            baseMapper.selectPage(pageParam,wrapper);

        }
    }

    //查询热门课程
    @Cacheable(value = "course",key="'selectTeacher'")
    @Override
    public List<EduCourse> getNewCourse() {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCourse = baseMapper.selectList(wrapper);
        return eduCourse;
    }

    //根据课程id查询分类
    @Override
    public CourseInfoFrontVo getCourseBaseInfo(String courseId) {
        return baseMapper.getCourseBaseInfo(courseId);
    }


    //全部查询

}
