package com.atguigu.code.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.code.excelwrite.DataDemo;

import java.util.Map;

public class readListener extends AnalysisEventListener<DataDemo> {
    @Override
    //获取excel的内容。invoke是一行一行的读出来,并放到dataDemo
    public void invoke(DataDemo dataDemo, AnalysisContext analysisContext) {
        System.out.println("dataDemo = " + dataDemo);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("headMap = " + headMap);
    }
    
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
