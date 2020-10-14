package com.zjjhyzd.dubbo.payment.service.impl;

import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.zjjhyzd.dubbo.payment.entity.OrderModel;
import com.zjjhyzd.dubbo.payment.entity.WxPayBean;
import com.zjjhyzd.dubbo.payment.service.IWxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@DubboService
@Slf4j
public class WxPayServiceImpl implements IWxPayService {

    @Autowired
    private WxPayBean wxPayBean;

    private String notifyUrl;

    @Override
    public WxPayApiConfig getApiConfig() {
        return WxPayApiConfig.builder()
                .appId(wxPayBean.getAppId())
                .mchId(wxPayBean.getMchId())
                .partnerKey(wxPayBean.getPartnerKey())
                .certPath(wxPayBean.getCertPath())
                .domain(wxPayBean.getDomain())
                .build();
    }

    @Override
    public Map<String, String> unifiedorder(OrderModel orderModel, boolean isSandBox) {

        WxPayApiConfig wxPayApiConfig = getApiConfig();

        notifyUrl = wxPayApiConfig.getDomain() + orderModel.getNotifyUrl();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(orderModel.getBody())
                .attach(orderModel.getAttach())
                .out_trade_no(orderModel.getOutTradeNo())
                .total_fee(orderModel.getTotalFee())
                .spbill_create_ip(orderModel.getIp())
                .notify_url(notifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(isSandBox, params);

        log.info("统一下单:" + xmlResult);

        return WxPayKit.xmlToMap(xmlResult);
    }

    @Override
    public String verifyNotify(Map<String, String> params) {

        String returnCode = params.get("return_code");

        if (WxPayKit.verifyNotify(params, getApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                // 发送通知等
                Map<String, String> xml = new HashMap<>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }
}
