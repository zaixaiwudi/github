package com.atguigu.common.servicebase.exception;

import com.atguigu.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//全局统一异常处理
@ControllerAdvice  //利用aop
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());  //把异常信息写到文件中
        return Result.error().message("全局异常处理");

/*        @ExceptionHandler(GuliException.class)
        @ResponseBody
        public Result error(Exception e){
            e.printStackTrace();
            return Result.error();*/

    }
}

/*    //自定义异常
    @ControllerAdvice  //利用aop
    public class GuliExceptionHandler {

        }*/

