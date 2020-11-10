package com.atguigu.serviceedu.controller;


import com.alibaba.excel.EasyExcel;
import com.atguigu.Result;
import com.atguigu.serviceedu.entity.subjectvo.OneSubjectVo;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.handler.ExcelListener;
import com.atguigu.serviceedu.handler.ExcelListener2;
import com.atguigu.serviceedu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-10-26
 */
@RestController
@RequestMapping("/serviceedu/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private ExcelListener excelListener;
    @Autowired
    private EduSubjectService subjectService;

    //课程分类列表
    @GetMapping("getSubjectList")
    public Result getSubjectList(){
        List<OneSubjectVo> list = subjectService.getAllSubjectList();
        return Result.ok().data("subjectList",list);
    }

    //添加课程分类
    @PostMapping("addSubject")
    //得到上传文件MultipartFile
    public Result addSubject(MultipartFile file){
        //获取上传文件的输入流
        //创建和excel对应的实体类
        //创建读取监听器
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectReadVo.class, excelListener).sheet().doRead();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}

