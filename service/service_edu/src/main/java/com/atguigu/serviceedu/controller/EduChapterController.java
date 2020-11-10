package com.atguigu.serviceedu.controller;


import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/serviceedu/chapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //查询课程里面的所有章节和小节数据
    @GetMapping("getChaptervideoByCourse/{courseId}")
    public Result getChaptervideoByCourse(@PathVariable String courseId ){
        List<ChapterVo> list = chapterService.getAllChapterVideo(courseId);
        return Result.ok().data("chapterVideo",list);
    }

    //添加章节
    @PostMapping("addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return Result.ok();
    }

    //删除章节
    @DeleteMapping("{id}")
    public Result deleteChapter(@PathVariable String id){
        chapterService.removeChapter(id);
        return Result.ok();
    }

    //根据id查询
    @GetMapping("getChapterById/{id}")
    public Result getChapterById(@PathVariable String id){
        EduChapter eduChapter = chapterService.getById(id);
        return Result.ok().data("eduChapter",eduChapter);
    }

    //修改章节
    @PostMapping("updateChapter")
    public Result updeteChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return Result.ok();
}

}

