package com.zjjhyzd.dubbo.payment;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class WechatServiceConsumerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatServiceConsumerDemoApplication.class, args);
    }
}
