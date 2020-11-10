package com.atguigu.ordersservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.servicebase.exception.GuliException;
import com.atguigu.ordersservice.entity.Order;
import com.atguigu.ordersservice.entity.PayLog;
import com.atguigu.ordersservice.mapper.PayLogMapper;
import com.atguigu.ordersservice.service.OrderService;
import com.atguigu.ordersservice.service.PayLogService;
import com.atguigu.ordersservice.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-06
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;


    @Override
    public Map createNative(String orderNo) {

        //根据订单号查询订单信息
        try {
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);

            Order order = orderService.getOne(wrapper);

            //调用微信支付的接口生成二维码
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");  //支付id
            m.put("mch_id", "1558950191");  // 商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());  //随机数
            m.put("body", order.getCourseTitle());  //课程名称
            m.put("out_trade_no", orderNo);   //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");  //订单金额
            m.put("spbill_create_ip", "127.0.0.1"); //客户端ip
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");  //二维码类型  固定金额

            //调用微信生成二维码的接口，设置xml格式
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            //把上面参数的map集合编程xml格式并且根据商户key进行加密处理
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.post();
            
            //得到请求返回数据。也是xml类型
            String xml = httpClient.getContent();
            //取xml值，转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("resultMap:::"+resultMap);

            //封装返回结果集,设置支付二维码页面所需要的值

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            //二维码生成状态，成功是success
            map.put("result_code", resultMap.get("result_code"));
            //二维码显示地址
            map.put("code_url", resultMap.get("code_url"));
            System.out.println("map===="+map);
            return map;
        } catch (Exception e) {
            throw new GuliException(20001,"失败");
        }

    }

    //调用微信接口，根据订单号查询状态
    @Override
    public Map<String, String> queryStatusPay(String orderNo) {
        //设置查询状态接口需要的参数

        try {
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //使用httpclient调用微信接口，传递参数。转换成xml格式并加密
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.post();
            //得到微信接口返回的数据
            String xml = httpClient.getContent();
            System.out.println("xmlllllllllllllllll = " + xml);
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            System.out.println("mappppppppppppppppp"+map);
            return map;
        } catch (Exception e) {
            throw new GuliException(20001,"查询失败");
        }

    }

    //当支付成功,更新状态，添加支付记录
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //订单号
        String orderNo = map.get("out_trade_no");
        //1.更新订单表订单状态status=1
        QueryWrapper<Order> wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        //intValue把包装类变成int
        if (order.getStatus().intValue()==1){
            return;
        }
        order.setStatus(1);

        //进行更新
        orderService.updateById(order);

        //2.想订单记录入职表中添加记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表


    }
}
