package com.atguigu.serviceedu.service.client;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFileDegradeFeignClient implements OrderClient {

    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
