package com.atguigu.ordersservice.service;

import com.atguigu.ordersservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    Map<String, String> queryStatusPay(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
