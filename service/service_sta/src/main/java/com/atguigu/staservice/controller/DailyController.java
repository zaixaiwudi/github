package com.atguigu.staservice.controller;


import com.atguigu.Result;
import com.atguigu.staservice.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/staservice/daily")
//@CrossOrigin
public class DailyController {

    @Autowired
    private DailyService dailyService;

    //生成统计数据
    @PostMapping("createStaData/{day}")
    public Result createSta(@PathVariable String day){
        dailyService.saveStaData(day);
        return Result.ok();
    }

    //返回图标显示需要的数据
    @GetMapping("showData/{begin}/{end}/{type}")
    public Result showChartData(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String,Object> map = dailyService.getShowData(begin,end,type);
        return Result.ok().data(map);
    }

}

