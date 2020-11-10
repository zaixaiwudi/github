package com.atguigu.serviceedu.controller;

import com.atguigu.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/serviceedu/user")
//@CrossOrigin   //允许跨域访问
public class EduloginController {

    @PostMapping("login")
    public Result login(){
        return Result.ok().data("token","admin");
    }

    //测试
    @GetMapping("info")
    public Result info(){
        return Result.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
