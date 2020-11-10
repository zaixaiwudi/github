package com.atguigu.serviceedu.service.client;

import com.atguigu.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//fallback表示熔断之后执行哪个实现类的方法
@FeignClient(name = "service-vod",fallback =VodFileDegradeFeignClient.class )
@Component
public interface VodClient {

    //定义调用接口路径。方法和参数
    //删除阿里云视频
    @DeleteMapping("/eduvod/vod/deleteVideo/{videoId}")
    //@PathVariable("videoId")必须加参数。不然报错
    public Result deleteVideo(@PathVariable("videoId") String videoId);

    //删除多个视频
    @DeleteMapping("/eduvod/vod/removeMoreVideo")
    public Result removeMoreVideo(@RequestParam("videoIdList") List<String> videoIdList);
}
