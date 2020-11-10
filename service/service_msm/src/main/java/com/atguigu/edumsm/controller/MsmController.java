package com.atguigu.edumsm.controller;

import com.atguigu.Result;
import com.atguigu.edumsm.service.MsmService;
import com.atguigu.edumsm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public Result sendMsm(@PathVariable String phone){
        //从redis获取验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return Result.ok();
        }
        code = RandomUtil.getFourBitRandom();

        boolean isSend = msmService.send(phone,code);
        if (isSend){
            //发送成功  5, TimeUnit.MINUTES设置有效时间
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
            return Result.ok();
        }else {
            return Result.error().message("发送短信失败");
        }
    }
}
