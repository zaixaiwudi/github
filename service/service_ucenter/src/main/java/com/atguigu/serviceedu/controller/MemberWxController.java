package com.atguigu.serviceedu.controller;

import com.atguigu.JwtUtils;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.Member;
import com.atguigu.serviceedu.service.MemberService;
import com.atguigu.serviceedu.utils.ConstantPropertiesUtil;
import com.atguigu.serviceedu.utils.HttpClientUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller  //RestController 可以返回数据
@RequestMapping("/api/ucenter/wx")
//@CrossOrigin
public class MemberWxController {

    @Autowired
    private MemberService memberService;
    //生成微信二维码
    //请求微信接口得到二维码
    @GetMapping("login")
    public String login(){
        //String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        //用占位符传值 %s
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        String wxOpenUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            wxOpenUrl = URLEncoder.encode(wxOpenUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //向%s设置值
        String url = String.format(baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                wxOpenUrl,
                "atguigu"
                );
        return "redirect:"+url;
    }

    //扫码之后跳转的方法
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session){

        try {
            //获取临时票据code，获取扫码人的信息
            //System.out.println("code = " + code);
            //System.out.println("state = " + state);
            //获取扫码人的信息(昵称、头像等)
            //添加到数据库
            //拿着临时票据code请求微信接口，得到access token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            baseAccessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code
            );
            //使用httpclient请求这个地址得到access token
            String result = HttpClientUtils.get(baseAccessTokenUrl);
            //System.out.println("@@@@@@@@@@@@@@@@@@@@@@"+result);
            //把result字符串转换成map集合，从map中获取值
            Gson gson = new Gson();
            HashMap resultMap = gson.fromJson(result, HashMap.class);
            //从map中获取值
            String access_token = (String) resultMap.get("access_token");
            String openid = (String) resultMap.get("openid");

            //判断openid是否存在数据库中，
            Member member = memberService.selectWxUserInfo(openid);
            if (member == null){
                //拿着access_token和openid得到扫码人的信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                baseUserInfoUrl = String.format(baseUserInfoUrl,
                        access_token,
                        openid
                );
                //使用httpclient请求这个地址得到扫码人的信息
                String userInfoResult = HttpClientUtils.get(baseUserInfoUrl);

                //从baseUserInfoUrl获取扫码人的信息
                HashMap userInfoMap = gson.fromJson(userInfoResult, HashMap.class);
                //昵称
                String nickname = (String) userInfoMap.get("nickname");
                //System.out.println(nickname);
                //头像
                String headimgurl = (String) userInfoMap.get("headimgurl");
                //System.out.println(headimgurl);

                //把信息添加到数据库中
                member = new Member();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                memberService.save(member);
            }

            //实现前端页面显示（登录）,返回token
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //跳转到首页面
            return "redirect:http://localhost:3000?token="+token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"扫码失败");
        }

    }
}
