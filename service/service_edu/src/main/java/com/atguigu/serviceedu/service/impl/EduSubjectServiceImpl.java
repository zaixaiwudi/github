package com.atguigu.serviceedu.service.impl;

import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.subjectvo.OneSubjectVo;
import com.atguigu.serviceedu.entity.subjectvo.TwoSubjectVo;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-26
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


/*    @Override
    public List<OneSubjectVo> getAllSubjectList() {

        //查询一级分类
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper();
        wrapper1.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper1);
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper();
        //查询2级分类
        wrapper2.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);

        List<OneSubjectVo> finalList = new ArrayList<>();
        List<TwoSubjectVo> twoList = new ArrayList<>();

        for (EduSubject oneSubject : oneSubjectList) {
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
            BeanUtils.copyProperties(oneSubject, oneSubjectVo);
            finalList.add(oneSubjectVo);
            for (EduSubject twoSubject : twoSubjectList) {
                TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                BeanUtils.copyProperties(twoSubject, twoSubjectVo);
                twoList.add(twoSubjectVo);
                if (oneSubject.getId()==twoSubject.getParentId()){
                    oneSubjectVo.setChildren(twoList);
                }
            }
        }
        return finalList;
    }*/

    @Override
    public List<OneSubjectVo> getAllSubjectList() {
        //查询所有一级分类
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper();
        wrapper1.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper1);

        //查询所有二级分类
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper();
        wrapper2.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);

        List<OneSubjectVo> finalSubjectList = new ArrayList<>();


        //封装1级分类,遍历一级分类
        for (EduSubject oneEduSubject : oneSubjectList) {

            //把每个oneEduSubject对象转换成OneSubjectVo对象
            OneSubjectVo oneSubjectVo = new OneSubjectVo();

            //oneSubjectVo.setId(oneEduSubject.getId());
            //oneSubjectVo.setTitle(oneEduSubject.getTitle());

            //简化上面两个步骤
            BeanUtils.copyProperties(oneEduSubject, oneSubjectVo);
            //把转换之后的vo对象放到finalSubjectList
            finalSubjectList.add(oneSubjectVo);

            //用于封装以及里面的所有二级分类
            List<TwoSubjectVo> twoList = new ArrayList<>();
            //封装二级分类
            for (EduSubject twoSubject : twoSubjectList) {
                //判断二级分类是否是上面一级分类里的二级
                if (oneEduSubject.getId().equals(twoSubject.getParentId())){
                    //
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                    BeanUtils.copyProperties(twoSubject, twoSubjectVo);
                    twoList.add(twoSubjectVo);
                }
            }
            //把封装之后的二级分类的集合放到一级分类中
            oneSubjectVo.setChildren(twoList);
        }
        return finalSubjectList;
    }
}
