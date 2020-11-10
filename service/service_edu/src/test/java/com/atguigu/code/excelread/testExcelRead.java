package com.atguigu.code.excelread;

import com.alibaba.excel.EasyExcel;
import com.atguigu.code.excelwrite.DataDemo;

public class testExcelRead {
    public static void main(String[] args) {
        String filename = "D:\\11.xlsx";
        EasyExcel.read(filename, DataDemo.class,new ReadListener()).sheet().doRead();
    }
}
