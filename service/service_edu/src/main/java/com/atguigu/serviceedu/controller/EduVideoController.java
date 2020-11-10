package com.atguigu.serviceedu.controller;


import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import com.sun.javafx.logging.JFRPulseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/serviceedu/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;
    //添加小节
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return Result.ok();
    }

    //删除小节
    @DeleteMapping("deleteVideo/{id}")
    public Result deleteVideo(@PathVariable String id){
        videoService.removeById(id);
        return Result.ok();
    }

    //根据id查询
    @GetMapping("getVideo/{id}")
    public Result getVideo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return Result.ok().data("eduVideo",eduVideo);
    }

    //修改
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return Result.ok();
    }


    //根基id查询小节数据
    @GetMapping("getEduVideo/{id}")
    public Result getEduVideo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return Result.ok().data("eduVideo",eduVideo);
    }

    //根据小节id删除小节
    @DeleteMapping("{id}")
    public Result deleteEduVideo(@PathVariable String id){
        videoService.deleteEduVideo(id);
        return Result.ok();
    }

}

