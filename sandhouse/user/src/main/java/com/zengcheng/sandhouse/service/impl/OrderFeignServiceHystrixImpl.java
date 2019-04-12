package com.zengcheng.sandhouse.service.impl;

import com.zengcheng.sandhouse.service.OrderFeignService;
import org.springframework.stereotype.Component;

/**
 * OrderFeignService断路器实现
 * @author zengcheng
 * @date 2019/4/12
 */
@Component
public class OrderFeignServiceHystrixImpl implements OrderFeignService {

    @Override
    public String sayHiFromOrderModule() {
        return "sorry,order module has no response!";
    }
}
