package com.atguigu.ordersservice.service.impl;

import com.atguigu.ordersservice.entity.Order;
import com.atguigu.ordersservice.mapper.OrderMapper;
import com.atguigu.ordersservice.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
