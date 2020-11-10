package com.atguigu.serviceedu.controller;


import com.atguigu.Result;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQueryVo;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *XMLHttpRequest是ajax在js中的核心函数
 * @author atguigu
 * @since 2020-10-20
 */

//@CrossOrigin
@Api(description="讲师管理")
@RestController
@RequestMapping("/serviceedu/teacher")
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public Result TeacherFindAll() {
        List<EduTeacher> list = eduTeacherService.list(null);
        //Result result = new Result();
        return Result.ok().data("list",list);
    }

    @ApiOperation(value = "删除讲师")
    @DeleteMapping("{id}")
    public Result removeById(@PathVariable String id){

        //boolean b = eduTeacherService.removeById(id);
        //优化
        boolean a = eduTeacherService.deleteTeacherById(id);
        if (a){
            return Result.ok();
        }else {
            return Result.error();
        }
    }


    //分页查询讲师
    @GetMapping("page/{current}/{limit}")
    public Result pageTeacher(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> pageParm = new Page<>(current,limit);
        eduTeacherService.page(pageParm,null);
        long total = pageParm.getTotal();
        List<EduTeacher> records = pageParm.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }

    //条件查询带分页,使用RequestBody必须是post提交
    @PostMapping("pagecondition/{current}/{limit}")
    public Result pageConditionTeacher(@RequestBody(required = false)TeacherQueryVo teacherQueryVo, @PathVariable long current,@PathVariable long limit){
         //分页page对象
        Page<EduTeacher> pageParam = new Page<>(current,limit);


        eduTeacherService.teacherPageList(pageParam,teacherQueryVo);

        long total = pageParam.getTotal();
        List<EduTeacher> records = pageParam.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }


    //添加讲师

    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    //根据id查询
    @GetMapping("getTeacher/{id}")
    public Result getTeacherInfo(@PathVariable String id){
        /*try {
            int a = 10/0;
        }catch (Exception e){
            throw new GuliException(20001,"执行了自动以异常");
        }
*/

        EduTeacher byId = eduTeacherService.getById(id);
        return Result.ok().data("teacher",byId);
    }

    //修改讲师,因为想用RequestBody所以用post
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.updateById(eduTeacher);
        if (save){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

}

