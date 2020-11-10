package com.atguigu.serviceedu.service.client;

import com.atguigu.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//fallback表示熔断之后执行哪个实现类的方法
@FeignClient(name = "service-order",fallback = OrderFileDegradeFeignClient.class)
@Component
public interface OrderClient {

    //根据课程id用户id查询订单
    @GetMapping("/ordersservice/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,@PathVariable("memberId") String memberId);

}
