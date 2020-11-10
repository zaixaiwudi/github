package com.atguigu.serviceedu.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.SubjectReadVo;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExcelListener2 extends AnalysisEventListener<SubjectReadVo> {
    @Autowired
    private EduSubjectService subjectService;
    @Override
    public void invoke(SubjectReadVo subjectReadVo, AnalysisContext analysisContext) {
        //获取一级分类
        String oneLevelSubject = subjectReadVo.getOneLevelSubject();
        //判断一级分类是否重复
        EduSubject one = this.one(oneLevelSubject);
        if (one==null){
            one =  new EduSubject();
            one.setTitle(oneLevelSubject);
            one.setParentId("0");
            subjectService.save(one);
        }
        //调用service进行添加


        //添加完成后将id作为父id来使用
        String pid = one.getId();


        //获取二级分类
        String twoLevelSubject = subjectReadVo.getTwoLevelSubject();
        EduSubject two = this.two(twoLevelSubject, pid);
        if (two==null){
            //调用service
            two = new EduSubject();
            two.setTitle(twoLevelSubject);
            two.setParentId(pid);
            subjectService.save(two);
        }

    }

    public EduSubject one(String oneName){

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", oneName);
        wrapper.eq("parent_id", 0);
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    public EduSubject two(String oneName,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", oneName);
        wrapper.eq("parent_id", pid);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
