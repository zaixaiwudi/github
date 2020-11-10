package com.atguigu.ordersservice.client;

import com.atguigu.Result;
import com.atguigu.vo.CourseInfoOrders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
@Component
public interface EduClient {

    //返回订单需要的课程信息
    @GetMapping("serviceedu/frontcourse/getCourseInfoOrders/{courseId}")
    public CourseInfoOrders getCourseInfoOrders(@PathVariable("courseId") String courseId);
}
