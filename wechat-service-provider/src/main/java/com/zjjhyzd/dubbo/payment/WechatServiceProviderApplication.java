package com.zjjhyzd.dubbo.payment;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class WechatServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatServiceProviderApplication.class, args);
    }
}
