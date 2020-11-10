package com.atguigu.code.read;

import com.alibaba.excel.EasyExcel;
import com.atguigu.code.excelwrite.DataDemo;
import com.atguigu.serviceedu.handler.ExcelListener;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class ExcelRead {
    @Test
    public void test(MultipartFile file){
        String fileName = "D:\\11.xlsx";
        EasyExcel.read(fileName, DataDemo.class,new readListener()).sheet().doRead();
    }
}
