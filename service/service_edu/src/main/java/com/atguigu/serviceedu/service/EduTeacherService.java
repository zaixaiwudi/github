package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-20
 */
public interface EduTeacherService extends IService<EduTeacher> {

    boolean deleteTeacherById(String id);

    void teacherPageList(Page<EduTeacher> pageParam, TeacherQueryVo teacherQueryVo);

    List<EduTeacher> getNewTeacher();
}
