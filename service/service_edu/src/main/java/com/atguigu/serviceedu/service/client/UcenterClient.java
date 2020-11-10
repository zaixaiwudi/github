package com.atguigu.serviceedu.service.client;

import com.atguigu.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    @GetMapping("/ucenterservice/member/getInfoUcenter/{id}")
    public UcenterMember getUcenterPay(@PathVariable("id") String id);
}
