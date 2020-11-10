package com.atguigu.staservice.client;

import com.atguigu.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UcenterClient {
    @GetMapping("/ucenterservice/member/getRegisterCount/{day}")
    public Result getRegisterCountUser(@PathVariable("day") String day);
}
