package com.zengcheng.sandhouse;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户模块启动类
 * @author zengcheng
 * @date 2019/4/12
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableApolloConfig(value = {"hello.properties", "application.properties"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
