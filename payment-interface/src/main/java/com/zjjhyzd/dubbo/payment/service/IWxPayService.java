package com.zjjhyzd.dubbo.payment.service;

import com.ijpay.wxpay.WxPayApiConfig;
import com.zjjhyzd.dubbo.payment.entity.OrderModel;

import java.util.Map;

public interface IWxPayService {

    /**
     * 获取微信API配置
     *
     * @return 配置
     */
    WxPayApiConfig getApiConfig();

    /**
     * 统一下单
     *
     * @param orderModel 订单内容
     * @param isSandBox  沙盒环境
     * @return 下单结果
     */
    Map<String, String> unifiedorder(OrderModel orderModel, boolean isSandBox);

    /**
     * 校验下单回调参数
     * 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
     *
     * @param params 参数
     * @return 如果校验成功直接返回微信XML响应字符串，否则返回NULL
     */
    String verifyNotify(Map<String, String> params);
}
