package com.atguigu.serviceedu.mapper;

import com.atguigu.serviceedu.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-11-04
 */
public interface MemberMapper extends BaseMapper<Member> {

    Integer getCountRegisterData(String day);
}
