package com.atguigu.serviceedu.controller.front;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.JwtUtils;
import com.atguigu.Result;
import com.atguigu.serviceedu.entity.CourseInfoVo;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.entity.frontvo.CourseInfoFrontVo;
import com.atguigu.serviceedu.entity.frontvo.CourseQueryVo;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.client.OrderClient;
import com.atguigu.vo.CourseInfoOrders;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("serviceedu/frontcourse")
//@CrossOrigin
public class EduCourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    //条件分页curren当前页limit每页记录数
    @PostMapping("getPageCourse/{current}/{limit}")
    public Result getPageCourse(@RequestBody(required = false)CourseQueryVo courseQueryVo,
                                @PathVariable long current,
                                @PathVariable long limit){
        Page<EduCourse> pageParam = new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){
            wrapper.eq("subject_parent_id", courseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectId())){
            wrapper.eq("subject_id", courseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQueryVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }
        courseService.page(pageParam, wrapper);

        //把分页所有数据都要返回
        List<EduCourse> records = pageParam.getRecords();
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

        return Result.ok().data(map);
    }

    //根据课程id查询课程的详细信息
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId , HttpServletRequest request){
        //基本信息
        CourseInfoFrontVo courseInfoVo = courseService.getCourseBaseInfo(courseId);
        //章节小节部分
        List<ChapterVo> allChapterVideo = chapterService.getAllChapterVideo(courseId);

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId==null){
            return Result.ok().data("courseInfo", courseInfoVo).data("allChapterVideo",allChapterVideo);
        }

        boolean isbuyCourse = orderClient.isBuyCourse(courseId, memberId);
        return Result.ok().data("courseInfo", courseInfoVo).data("allChapterVideo",allChapterVideo).data("isbuyCourse",isbuyCourse);
    }

    //返回订单需要的课程信息
    @GetMapping("getCourseInfoOrders/{courseId}")
    public CourseInfoOrders getCourseInfoOrders(@PathVariable String courseId){
        CourseInfoFrontVo courseBaseInfo = courseService.getCourseBaseInfo(courseId);
        CourseInfoOrders courseInfoOrders = new CourseInfoOrders();
        BeanUtils.copyProperties(courseBaseInfo, courseInfoOrders);
        return courseInfoOrders;
    }
}
