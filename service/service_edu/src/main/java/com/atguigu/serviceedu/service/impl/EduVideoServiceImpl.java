package com.atguigu.serviceedu.service.impl;

import com.atguigu.Result;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.mapper.EduVideoMapper;
import com.atguigu.serviceedu.service.EduVideoService;
import com.atguigu.serviceedu.service.client.VodClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;
    //TODO小节中有视频

    //根据课程id删除小节
    @Override
    public void deleteVideoByCourseId(String courseId) {
        List<String> videoIds = new ArrayList<>();

        //根基课程id查询小节id并封装
        QueryWrapper<EduVideo> wrapperVod = new QueryWrapper<>();
        wrapperVod.eq("course_id", courseId);
        //指定查询的列
        wrapperVod.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapperVod);
        for (EduVideo eduVideo : eduVideos) {
            //获取每个小节对象。从每个小节对象里获取视频
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }

        if (videoIds.size()>0) {
            vodClient.removeMoreVideo(videoIds);
        }

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }


    @Override
    public void deleteEduVideo(String id) {
        //删除视频,根据小节id查询视频id
        EduVideo eduVideo = baseMapper.selectById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            //远程调用进行删除
            Result result = vodClient.deleteVideo(videoSourceId);
            if (result.getCode()!=20000){
                throw new GuliException(20001,"连接超时");
            }
        }

        //删除小节
        int i = baseMapper.deleteById(id);
        if (i<=0){
            throw new GuliException(20001,"删除失败");
        }

    }
}
