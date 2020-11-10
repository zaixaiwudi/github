package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.Member;
import com.atguigu.serviceedu.entity.vo.LoginVo;
import com.atguigu.serviceedu.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
public interface MemberService extends IService<Member> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    //判断openid是否存在数据库中，
    Member selectWxUserInfo(String openid);

    Integer getCountRegister(String day);
}
