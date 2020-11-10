package com.atguigu.serviceedu.service.impl;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQueryVo;
import com.atguigu.serviceedu.mapper.EduTeacherMapper;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-20
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //删除讲师
    @Override
    public boolean deleteTeacherById(String id) {
        //调用删除方法。返回影响行数
        int result = baseMapper.deleteById(id);
        return result>0;
    }

    //条件查询待分页
    @Override
    public void teacherPageList(Page<EduTeacher> pageParam, TeacherQueryVo teacherQueryVo) {
        //判断条件，封装构造器
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //获取条件值
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String begin = teacherQueryVo.getBegin();
        String end = teacherQueryVo.getEnd();
        //获取条件值
        if (!StringUtils.isEmpty(name)){
            //拼接条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)){
            //拼接条件
            wrapper.like("level", level);
        }
        if (!StringUtils.isEmpty(begin)){
            //拼接条件
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)){
            //拼接条件
            wrapper.le("gmt_modified", end);
        }
        baseMapper.selectPage(pageParam, wrapper);
    }

    //查询热门名师
    @Cacheable(value = "teacher",key = "'selectTeacher'")
    @Override
    public List<EduTeacher> getNewTeacher() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> teacherList = baseMapper.selectList(wrapper);
        return teacherList;
    }
}
