package com.atguigu.code.excelwrite;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DataDemo {

    //设置表头名称
@ExcelProperty(index = 0)
    private int sno;

    //设置表头名称
@ExcelProperty(index = 1)
    private String sname;
}
