package com.atguigu.code.excelread;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.code.excelwrite.DataDemo;

import java.sql.SQLOutput;
import java.util.Map;

public class ReadListener extends AnalysisEventListener<DataDemo> {
    @Override
    public void invoke(DataDemo dataDemo, AnalysisContext analysisContext) {
        //当我们创建监听器对象。执行此方法，把excel里的内容一行一行读取
        //把每行读取的内容封装到DataDemo
        System.out.println(dataDemo);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头是"+headMap);


    }


        @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    //读取之后执行此方法
    }
}
