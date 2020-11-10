package com.atguigu.cmsservice.service;

import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.entity.Vo.CrmBannerVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> selectIndexBanner();

    //添加幻灯片
    void insertNewBanner(CrmBanner crmBanner);

    void getBannerPageList(CrmBannerVo crmBanner, Page<CrmBanner> pageParams);

    boolean deleteBannerById(String bannerId);


    CrmBanner getBannerById(String bannerId);


}
