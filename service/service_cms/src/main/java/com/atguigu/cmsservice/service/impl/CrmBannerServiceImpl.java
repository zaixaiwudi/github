package com.atguigu.cmsservice.service.impl;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.entity.Vo.CrmBannerVo;
import com.atguigu.cmsservice.mapper.CrmBannerMapper;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<CrmBanner> selectIndexBanner() {
        //首先查缓存，有直接返回。没有查数据库。。再放到缓存中
        //根据key取值
        List<CrmBanner> crmBanners = (List<CrmBanner>) redisTemplate.opsForValue().get("banner");
        if (crmBanners==null){
            QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            //last有sql注入问题，不传参数
            wrapper.last("limit 3");
            crmBanners = baseMapper.selectList(wrapper);
            //把数据放到缓存中
            redisTemplate.opsForValue().set("banner",crmBanners);
        }
        return crmBanners;
    }

    @Override
    public void insertNewBanner(CrmBanner crmBanner) {
        baseMapper.insert(crmBanner);
    }

    @Override
    public void getBannerPageList(CrmBannerVo crmBannerVo, Page<CrmBanner> pageParams) {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper();
        String title = crmBannerVo.getTitle();
        Date gmtModified = crmBannerVo.getGmtModified();
        Date gmtCreate = crmBannerVo.getGmtCreate();
        if (!StringUtils.isEmpty(title)){
        wrapper.like("title", crmBannerVo.getTitle());
        }
        if (!StringUtils.isEmpty(gmtCreate)){
            wrapper.ge("gmt_create",gmtCreate);
        }
        if (!StringUtils.isEmpty(gmtModified)){
            wrapper.le("gmt_modified", gmtModified);
        }
        baseMapper.selectPage(pageParams, wrapper);
    }

    @Override
    public boolean deleteBannerById(String bannerId) {
        int i = baseMapper.deleteById(bannerId);
        return i>0;
    }

    @Override
    public CrmBanner getBannerById(String bannerId) {

        CrmBanner crmBanner = baseMapper.selectById(bannerId);
        return crmBanner;
    }




}
