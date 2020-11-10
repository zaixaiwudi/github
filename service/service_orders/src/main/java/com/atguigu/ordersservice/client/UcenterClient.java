package com.atguigu.ordersservice.client;

import com.atguigu.Result;
import com.atguigu.vo.MemberOrders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UcenterClient {

    //返回订单需要的课程信息
    @GetMapping("/ucenterservice/member/getInfoUc/{id}")
    public MemberOrders getInfoUsers(@PathVariable("id") String courseId);
}
