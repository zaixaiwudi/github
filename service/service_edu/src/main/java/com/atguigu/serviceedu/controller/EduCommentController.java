package com.atguigu.serviceedu.controller;


import com.atguigu.JwtUtils;
import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduComment;
import com.atguigu.serviceedu.service.EduCommentService;
import com.atguigu.serviceedu.service.client.UcenterClient;
import com.atguigu.vo.UcenterMember;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/serviceedu/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id查询评论列表带分页
    @GetMapping("{page}/{limit}/{courseId}")
    public Result index(@PathVariable Long page,@PathVariable Long limit,@PathVariable String courseId){
        Page<EduComment> pageParam = new Page<>(page,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        commentService.page(pageParam, wrapper);

        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return Result.ok().data(map);

    }
    //添加评论
    @PostMapping("auth/save")
    public Result save(@RequestBody EduComment comment, HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(id)){
            return Result.error().code(20001).message("请先登录");
        }
        comment.setMemberId(id);
        //得到用户信息
        UcenterMember ucenterMember = ucenterClient.getUcenterPay(id);
        //BeanUtils.copyProperties(ucenterMember, comment);
        comment.setNickname(ucenterMember.getNickname());
        comment.setAvatar(ucenterMember.getAvatar());
        commentService.save(comment);
        return Result.ok();
    }

}

