package com.atguigu.serviceedu.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class ExcelListener extends AnalysisEventListener<SubjectReadVo> {

    @Autowired
    private EduSubjectService subjectService;
    @Override
    //invoke会一行一行读取excel的内容
    public void invoke(SubjectReadVo subjectReadVo, AnalysisContext analysisContext) {
        //System.out.println(subjectReadVo);
        //调用service，调用mapper添加数据库
        //添加一级分类
        //1.1 subjectReadVo获取到读取的每个1级分类名称
        String oneLevelSubject = subjectReadVo.getOneLevelSubject();
        //判断以及分类是否重复
        EduSubject eduSubjectExist = this.existOneSubject(oneLevelSubject);
        if (eduSubjectExist==null){
            //1.2 调用service添加一级分类到数据库中
            eduSubjectExist = new EduSubject();
            eduSubjectExist.setTitle(oneLevelSubject);
            eduSubjectExist.setParentId("0");
            //排序
            eduSubjectExist.setSort(0);
            subjectService.save(eduSubjectExist);
        }


        //添加完成之后的一级分类id
        String pid = eduSubjectExist.getId();

        //添加二级分类，并且有层级关系
        //2.1subjectReadVo获取到读取的每个2级分类名称
        String twoLevelSubject = subjectReadVo.getTwoLevelSubject();

        //判断二级分类是否重复
        EduSubject eduSubjectTwoExist = this.existTwoSubject(twoLevelSubject, pid);
        if (eduSubjectTwoExist==null){
            //2.2 调用service添加er级分类到数据库中
            eduSubjectTwoExist = new EduSubject();
            eduSubjectTwoExist.setTitle(twoLevelSubject);
            eduSubjectTwoExist.setParentId(pid);
            eduSubjectTwoExist.setSort(0);
            subjectService.save(eduSubjectTwoExist);
        }

    }


    //判断一级分类是否重复
    private EduSubject existOneSubject(String oneName){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",oneName);
        wrapper.eq("parent_id", 0);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    //判断er级分类是否重复
    private EduSubject existTwoSubject(String twoName,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",twoName);
        wrapper.eq("parent_id", pid);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
