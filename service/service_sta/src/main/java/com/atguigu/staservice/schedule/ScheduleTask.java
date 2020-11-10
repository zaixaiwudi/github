package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.DailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {
    @Autowired
    private DailyService dailyService;

    //每天凌晨1点执行生成统计数据方法,统计前一天的数据
    @Scheduled(cron = "0 0 1 * * ? ")
    public void taskstadata(){
        //得到当前日期前一天的数据
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.saveStaData(day);
    }
}
