package com.atguigu.serviceedu.service.client;

import com.atguigu.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public Result deleteVideo(String videoId) {
        return Result.error().message("time out");
    }

    @Override
    public Result removeMoreVideo(List<String> videoIdList) {
        return Result.error().message("time out");
    }
}
