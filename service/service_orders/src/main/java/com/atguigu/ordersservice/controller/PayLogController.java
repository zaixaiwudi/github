package com.atguigu.ordersservice.controller;


import com.atguigu.Result;
import com.atguigu.ordersservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/ordersservice/paylog")
//@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码
    @GetMapping("createNative/{orderNo}")
    public Result createNative(@PathVariable String orderNo) {
        Map map = payLogService.createNative(orderNo);
        return Result.ok().data(map);
    }

    //根据订单号查询订单支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo){
        //调用微信接口，根据订单号查询状态
        Map<String,String> map = payLogService.queryStatusPay(orderNo);
        //如果接口返回map为空。则失败
        if (map==null){
            return Result.error().message("支付出错");
        }
        //当支付成功,更新状态，添加支付记录
        if (map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrderStatus(map);
            return Result.ok().message("支付成功");
        }
        //正在支付中
        return Result.ok().code(25000).message("支付中");
    }

}

