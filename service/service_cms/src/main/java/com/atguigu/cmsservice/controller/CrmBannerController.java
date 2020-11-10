package com.atguigu.cmsservice.controller;


import com.atguigu.Result;
import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.entity.Vo.CrmBannerVo;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-02
 */
@RestController
@RequestMapping("/cmsservice/banner")
//@CrossOrigin
public class CrmBannerController {


    @Autowired
    private CrmBannerService bannerService;

    //查询幻灯片
    @GetMapping("selectNewBanner")
    public Result selectNewBanner(){

        List<CrmBanner> list = bannerService.selectIndexBanner();
        return Result.ok().data("bannerList",list);
    }

    //添加幻灯片
    @PostMapping("addNewBanner")
    public Result addNewBanner(@RequestBody CrmBanner crmBanner){
        bannerService.insertNewBanner(crmBanner);
        return Result.ok();
    }

    //条件查询分页带幻灯片
    @PostMapping("pageBanner/{current}/{limit}")
    public Result pageBanner(@RequestBody CrmBannerVo crmBannerVo, @PathVariable Long current, @PathVariable Long limit){
        Page<CrmBanner> pageParams = new Page(current,limit);
        bannerService.getBannerPageList(crmBannerVo,pageParams);
        long total = pageParams.getTotal();
        List<CrmBanner> records = pageParams.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }

    //删除幻灯片
    @DeleteMapping("deleteBanner/{bannerId}")
    public Result deleteBanner(@PathVariable String bannerId){
        boolean a = bannerService.deleteBannerById(bannerId);
        if (a){
            return Result.ok();
        }else {
            return Result.error();
        }

    }

    //数据回显
    @GetMapping("getBannerById/{bannerId}")
    public Result getBannerById(@PathVariable String bannerId){
        CrmBanner crmBanner = bannerService.getBannerById(bannerId);
        return Result.ok().data("crmBanner",crmBanner);
    }

    //修改幻灯片
    @PostMapping("updateBanner")
    public Result updateBanner(@RequestBody CrmBanner crmBanner){
        boolean save = bannerService.updateById(crmBanner);
        if (save){
            return Result.ok();
        }else {
            return Result.error();
        }

    }

}

