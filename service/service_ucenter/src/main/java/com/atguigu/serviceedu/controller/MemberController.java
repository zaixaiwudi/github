package com.atguigu.serviceedu.controller;


import com.atguigu.JwtUtils;
import com.atguigu.Result;
import com.atguigu.serviceedu.entity.Member;
import com.atguigu.serviceedu.entity.vo.LoginVo;
import com.atguigu.serviceedu.entity.vo.RegisterVo;
import com.atguigu.serviceedu.service.MemberService;
import com.atguigu.vo.MemberOrders;
import com.atguigu.vo.UcenterMember;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/ucenterservice/member")
//@CrossOrigin
public class MemberController {

    @Autowired
    private MemberService memberService;

    //登录
    @PostMapping("login")
    public Result loginUser(@RequestBody LoginVo loginVo){
        //登录成功后获取token字符串
        String token = memberService.login(loginVo);
        return Result.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public Result registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return Result.ok();
    }

    //根据token获取用户信息
    @GetMapping("getLoginInfo")
    public Result getUserInfo(HttpServletRequest request){
        //调用jwt中的方法。获取token
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据用户id查询用户讯息
        Member member = memberService.getById(memberId);
        return Result.ok().data("member",member);
    }

    //根据用户id返回用户信息
    @GetMapping("getInfoUc/{id}")
    public MemberOrders getInfoUsers(@PathVariable String id){
        Member member = memberService.getById(id);
        MemberOrders memberOrders = new MemberOrders();
        BeanUtils.copyProperties(member, memberOrders);
        return memberOrders;
    }

    //查询某一天的注册人数
    @GetMapping("getRegisterCount/{day}")
    public Result getRegisterCountUser(@PathVariable String day){
        Integer count = memberService.getCountRegister(day);
        return Result.ok().data("RegisterCount",count);
    }

    //根据用户id查询用户信息
    @GetMapping("getInfoUcenter/{id}")
    public UcenterMember getInfo(@PathVariable String id){
        Member byId = memberService.getById(id);
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(byId, ucenterMember);
        return ucenterMember;
    }
}

