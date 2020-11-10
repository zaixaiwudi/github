package com.atguigu.serviceedu.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;

    @Value("${wx.open.app_secret}")
    private String appSecret;

    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;

    //初始化ConstantPropertiesUtil完成之后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;

        WX_OPEN_APP_SECRET = appSecret;

        WX_OPEN_REDIRECT_URL = redirectUrl;
    }
}
