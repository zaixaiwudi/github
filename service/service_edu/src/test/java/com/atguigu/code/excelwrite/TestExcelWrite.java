package com.atguigu.code.excelwrite;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestExcelWrite {
    public static void main(String[] args) {
        //有两个参数，1.excel的路劲和文件名称  2.实体类的class
        String filename = "D:\\11.xlsx";
        EasyExcel.write(filename,DataDemo.class).sheet("写操作").doWrite(getData());
    }
    private static List<DataDemo> getData(){
        List<DataDemo> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            DataDemo dataDemo = new DataDemo();
            dataDemo.setSno(i);
            dataDemo.setSname("在下无敌"+i);
            list.add(dataDemo);
        }
        return list;
    }
}
