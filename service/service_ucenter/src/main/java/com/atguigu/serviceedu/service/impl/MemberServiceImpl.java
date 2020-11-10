package com.atguigu.serviceedu.service.impl;

import com.atguigu.JwtUtils;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.Member;
import com.atguigu.serviceedu.entity.vo.LoginVo;
import com.atguigu.serviceedu.entity.vo.RegisterVo;
import com.atguigu.serviceedu.mapper.MemberMapper;
import com.atguigu.serviceedu.service.MD5;
import com.atguigu.serviceedu.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;
    //登录
    @Override
    public String login(LoginVo loginVo) {
        //判断手机号和密码是否为空
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"手机号或者密码为空");
        }
        //根据手机号查询用户信息
        QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
        wrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(wrapper);
        if (member==null){
            throw new GuliException(20001,"手机号为空");
        }
        //比较密码是否一样
        String encryptPassword = MD5.encrypt(password);
        if(!member.getPassword().equals(encryptPassword)){
            throw new GuliException(20001,"密码错误");
        }
        //查看用户是否禁用
        if(member.getIsDisabled()){
            throw new GuliException(20001,"用户已经禁用");
        }
        //登陆成功，根据用户信息使用jwt生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        //返回token字符串
        return jwtToken;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        System.out.println("=====================" + code);
        //判断注册信息是否为空
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)
        ||StringUtils.isEmpty(code)||StringUtils.isEmpty(nickname)
        ){
            throw new GuliException(20001,"注册信息为空");
        }
        //判断验证码是否一样
        //从redis中取出来
        String codeRedis = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(codeRedis)){
            throw new GuliException(20001,"验证码错误");
        }
        //判断手机号是否存在
        //根据手机号查询数据可是否存在
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new GuliException(20001,"手机号已经存在");
        }
        //ba注册信息添加数据库
        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        //密码需要加密
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);
    }

    //判断openid是否存在数据库中，
    @Override
    public Member selectWxUserInfo(String openid) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return baseMapper.selectOne(wrapper);
    }

    //查询某一天的注册人数
    @Override
    public Integer getCountRegister(String day) {
        return baseMapper.getCountRegisterData(day);
    }
}
