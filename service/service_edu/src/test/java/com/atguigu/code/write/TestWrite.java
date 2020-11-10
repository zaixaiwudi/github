package com.atguigu.code.write;

import com.alibaba.excel.EasyExcel;
import com.atguigu.code.excelwrite.DataDemo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {

    String firename = "D:\\11.xlsx";
    @Test
    public void testWrite(){
        EasyExcel.write(firename, DataDemo.class).sheet().doWrite(getData());
    }

    public static List getData(){

        List<DataDemo> list = new ArrayList<>();
        for (int i = 0;i<5;i++){
            DataDemo dataDemo = new DataDemo();
            dataDemo.setSname("我是真的猛"+i);
            dataDemo.setSno(i);
            list.add(dataDemo);

        }
        return list;
}
}
