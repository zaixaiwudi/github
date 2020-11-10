package com.atguigu.edumsm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.edumsm.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
        try {
            if (StringUtils.isEmpty(phone)){
                return false;
            }
            DefaultProfile profile =
                    DefaultProfile.getProfile("default", "LTAI4GCi5wcLTsAAtprP6nNb", "t5ZDHHR7809TJPw04fW2hRDCzO6Tb8");
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");

            request.putQueryParameter("PhoneNumbers",phone); //手机号
            request.putQueryParameter("SignName", "我的在线教育学习网站"); //签名名称
            request.putQueryParameter("TemplateCode", "SMS_205403063");  //模板code
            //code应该用json形式传递
            Map<String,Object> map = new HashMap<>();
            map.put("code", code);
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map)); //验证码值
            CommonResponse response = client.getCommonResponse(request);

            return response.getHttpResponse().isSuccess();
        } catch (ClientException e){

            e.printStackTrace();
            throw new GuliException(20001,"发送失败");
        }
    }
}
