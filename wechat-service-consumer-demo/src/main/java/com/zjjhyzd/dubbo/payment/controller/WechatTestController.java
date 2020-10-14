package com.zjjhyzd.dubbo.payment.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.QrCodeKit;
import com.ijpay.core.kit.WxPayKit;
import com.zjjhyzd.dubbo.payment.entity.OrderModel;
import com.zjjhyzd.dubbo.payment.service.IWxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/wxPay")
@Slf4j
public class WechatTestController {

    @DubboReference
    private IWxPayService wxPayService;

    /**
     * 扫码模式 统一下单接口
     */
    @RequestMapping(value = "/scanCode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void scanCode1(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("total_fee") String totalFee) throws IOException {
        if (StringUtils.isBlank(totalFee)) {
            throw new RuntimeException("支付金额不能为空");
        }

        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        OrderModel orderModel = new OrderModel();
        orderModel.setAttach("Node.js 版:https://gitee.com/javen205/TNWX")
                .setBody("IJPay 让支付触手可及-公众号支付")
                .setIp(ip)
                .setNotifyUrl("/wxPay/payNotify")
                .setOutTradeNo(WxPayKit.generateStr())
                .setTotalFee(totalFee);

        Map<String, String> result = wxPayService.unifiedorder(orderModel, false);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");

        if (!WxPayKit.codeIsOk(returnCode)) {
            throw new RuntimeException("error:" + returnMsg);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            throw new RuntimeException("error:" + returnMsg);
        }
        //生成预付订单success
        QrCodeKit.encodeOutPutSteam(response.getOutputStream(), result.get("code_url"), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200);
    }


    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        return wxPayService.verifyNotify(WxPayKit.xmlToMap(xmlMsg));
    }
}
