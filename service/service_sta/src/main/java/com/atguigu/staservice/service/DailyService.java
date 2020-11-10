package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-09
 */
public interface DailyService extends IService<Daily> {

    void saveStaData(String day);

    //返回图标显示需要的数据
    Map<String, Object> getShowData(String begin, String end, String type);
}
