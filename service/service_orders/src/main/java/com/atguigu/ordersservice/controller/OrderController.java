package com.atguigu.ordersservice.controller;


import com.atguigu.JwtUtils;
import com.atguigu.Result;
import com.atguigu.ordersservice.client.EduClient;
import com.atguigu.ordersservice.client.UcenterClient;
import com.atguigu.ordersservice.entity.Order;
import com.atguigu.ordersservice.service.OrderService;
import com.atguigu.ordersservice.utils.OrderNoUtil;
import com.atguigu.vo.CourseInfoOrders;
import com.atguigu.vo.MemberOrders;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/ordersservice/order")
//@CrossOrigin
public class OrderController {
    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private OrderService orderService;
    //根据课程id生成课程订单
    @PostMapping("createOrders/{courseId}")
    public Result createOrdersByCourse(@PathVariable String courseId ,HttpServletRequest request){
        /*Result result = eduClient.getCourseInfoOrders(courseId);
        CourseInfoOrders courseInfoOrders = (CourseInfoOrders) result.getData().get("ordersCourseInfo");

        System.out.println(courseInfoOrders);*/
        
        /*MemberOrders infoUsers = ucenterClient.getInfoUsers(id);
        MemberOrders memberOrders = (MemberOrders) resultUserInfo.getData().get("memberOrders");
        System.out.println(memberOrders);*/

        CourseInfoOrders courseInfoOrders = eduClient.getCourseInfoOrders(courseId);
        //根据用户id获取用户信息
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //String id = "1";
        MemberOrders memberOrders = ucenterClient.getInfoUsers(id);
        Order order = new Order();
        String orderNo = OrderNoUtil.getOrderNo();
        //System.out.println("orderNo = " + orderNo);
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrders.getTitle());
        order.setCourseCover(courseInfoOrders.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoOrders.getPrice());
        order.setMemberId(id);
        order.setMobile(memberOrders.getMobile());
        order.setNickname(memberOrders.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        orderService.save(order);
        return Result.ok().data("orderId",orderNo);
    }
    //根据订单号查询订单信息
    @GetMapping("getOrdersInfo/{orderNo}")
    public Result getOrdersInfoCourse(@PathVariable String orderNo){
        System.out.println("orderNo = " + orderNo);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        //System.out.println("-------------------------"+order);
        return  Result.ok().data("orders",order);
    }

    //根据课程id用户id查询订单
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        return count>0;
    }


}

