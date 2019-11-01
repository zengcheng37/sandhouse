package com.zengcheng.sandhouse.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 负载均衡的远程调用设置
 * @author zengcheng
 * @date 2019/10/28
 */
@Configuration
public class LoadBalancedRestConfig {

    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
