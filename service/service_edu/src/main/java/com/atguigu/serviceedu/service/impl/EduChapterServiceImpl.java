package com.atguigu.serviceedu.service.impl;

import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.chaptervo.ChapterVo;
import com.atguigu.serviceedu.entity.chaptervo.VideoVo;
import com.atguigu.serviceedu.mapper.EduChapterMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-10-27
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
//查询章节和小节
    @Autowired
    private EduVideoService videoService;
    @Override
    public List<ChapterVo> getAllChapterVideo(String courseId) {
        //genju 课程id查询课程里的章节
        QueryWrapper<EduChapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapper1);
        //2.cha 小节
        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapper2);

        //3.遍历章节结合，进行封装
        //创建集合作为最终
        List<ChapterVo> finalChapterVideoList = new ArrayList<>();

        for (EduChapter eduChapter : eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            //赋值数据后的vo放到集合中去
            finalChapterVideoList.add(chapterVo);
            //用于封装每个章节里的小节
            List<VideoVo> videoList = new ArrayList<>();
            for (EduVideo eduVideo : eduVideoList) {
                //
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoList.add(videoVo);
                }
            }
            //把小节集合放到章节中
            chapterVo.setChildren(videoList);
        }

        //4.
        return finalChapterVideoList;
    }

    //删除章节
    @Override
    public void removeChapter(String id) {
        //查询章节里是否有小节，有就不删除
        //1.查询小节表，根据章节id查询
        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("chapter_id", id);
        int count = videoService.count(wrapper);
        if (count>0){
            throw new GuliException(20001,"不能删除");
        }else {
            baseMapper.deleteById(id);
        }
    }

    @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
