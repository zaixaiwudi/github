package com.atguigu.staservice.service.impl;

import com.atguigu.Result;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.Daily;
import com.atguigu.staservice.mapper.DailyMapper;
import com.atguigu.staservice.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {


    @Autowired
    private UcenterClient ucenterClient;
    //生成统计数据
    @Override
    public void saveStaData(String day) {

        //添加之前先删除
        QueryWrapper<Daily> wrapper = new QueryWrapper();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);
        //将查询出来的数据添加到数据数据分析表中
        Result result = ucenterClient.getRegisterCountUser(day);
        Integer registerCount = (Integer) result.getData().get("RegisterCount");
        Daily daily = new Daily();
        daily.setRegisterNum(registerCount);
        daily.setCourseNum(RandomUtils.nextInt(100,200));
        daily.setVideoViewNum(RandomUtils.nextInt(100,200));
        daily.setLoginNum(RandomUtils.nextInt(100,200));
        daily.setDateCalculated(day);
        baseMapper.insert(daily);
    }

    //返回图标显示需要的数据
    @Override
    public Map<String, Object> getShowData(String begin, String end, String type) {
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        //查询某一个字段
        wrapper.select(type,"date_calculated");
        List<Daily> dailieList = baseMapper.selectList(wrapper);

        //dailieList有所有数据，，但是格式不符合前端要求
        //因为前端中需要两个list集合，一个是封装日期，一个封装对应数据
        List<String> date_calculated = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        //遍历所有数据list集合，想两个集合封装
        for (Daily daily : dailieList) {
            //封装日期数据
            date_calculated.add(daily.getDateCalculated());
            //封装日期对应的数据
            //判断选择查询哪个类型数据
            switch (type){
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        //将两个list集合放到map中，返回
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculated",date_calculated);
        map.put("dataList",dataList);
        return map;
    }
}
