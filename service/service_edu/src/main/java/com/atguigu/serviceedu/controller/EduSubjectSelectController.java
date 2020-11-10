package com.atguigu.serviceedu.controller;


import com.alibaba.excel.EasyExcel;
import com.atguigu.Result;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.subjectvo.OneSubjectVo;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.handler.ExcelListener;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
public class EduSubjectSelectController {


    @Autowired
    private EduSubjectService subjectService;

    //递归查询分类列表
    @GetMapping("getAllSubjectList")
    public Result getSubjectList(){
        //查询所有分类
        List<EduSubject> listAll = subjectService.list(null);
        //调用build方法把所有分类数据构建
        List<EduSubject> result = this.build(listAll);
        return Result.ok().data("result",result);
    }

    //参数是所有分类
    private List<EduSubject> build(List<EduSubject> listAll) {
        //封装最终数据
        List<EduSubject> finalList = new ArrayList<>();
        //遍历所有分类，得到一级分类
        for (EduSubject node : listAll) {
             //得到一级分类
            if ("0".equals(node.getParentId())){
                node.setLevel(1);
                //把得到的所有的一级分类放到finalList
                //
                finalList.add(this.findChildren(node,listAll));
            }
        }
        return finalList;
    }

    //查询一级下面的二级，二级下面的三级，递归查询
    //node是一级分类，listAll是所有分类
    private EduSubject findChildren(EduSubject node, List<  EduSubject> listAll) {

        for (EduSubject it : listAll) {
            //获取下级分类
            if (node.getId().equals(it.getParentId())){
                //获取上级的level值+1，设置到当前分类
               Integer level =  node.getLevel()+1;
                it.setLevel(level);
                //把分类放到上级分类
                List<EduSubject> children = node.getChildren();
                children.add(this.findChildren(it, listAll));
            }
        }
        return node;
    }



    //递归删除课程分类
    @DeleteMapping("removeSubject/{id}")
    public Result removeEduSubject(@PathVariable String id){
        this.deleteSubjectId(id);
        return Result.ok();
    }

    //递归删除课程分类
    private void deleteSubjectId(String id) {
        //获取当前删除的分类和分类下面的子分类所有id，放到list集合
        List<String> idList = new ArrayList<>();
        //调用方法，查子分类id
        this.selectChildListById(id,idList);
        idList.add(id);
        subjectService.removeByIds(idList);
    }

    private void selectChildListById(String id, List<String> idList) {
        //根据parent——id查询分类下面的子分类的id
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        wrapper.select("id");
        List<EduSubject> childList = subjectService.list(wrapper);
        //查询子分类集合遍历，得到zifenlid，放到idlist
        childList.stream().forEach(item->{
            //通过遍历item得到分类的id
            idList.add(item.getId());
            //递归
            this.selectChildListById(item.getId(),idList);
        });
    }
}

