package com.zjjhyzd.dubbo.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderModel implements Serializable {
    private String body;
    private String attach;
    private String outTradeNo;
    private String totalFee;
    private String ip;
    private String notifyUrl;

    public String getBody() {
        return body;
    }

    public OrderModel setBody(String body) {
        this.body = body;
        return this;
    }

    public String getAttach() {
        return attach;
    }

    public OrderModel setAttach(String attach) {
        this.attach = attach;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public OrderModel setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public OrderModel setTotalFee(String totalFee) {
        this.totalFee = new BigDecimal(totalFee).multiply(new BigDecimal("100")).toString();
        return this;
    }

    public String getIp() {
        return ip;
    }

    public OrderModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public OrderModel setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }
}
