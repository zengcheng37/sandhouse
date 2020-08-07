package com.zengcheng.sandhouse.feign.hystrix;

import com.zengcheng.sandhouse.feign.OrderFeignService;
import org.springframework.stereotype.Component;

/**
 * OrderFeignService断路器实现
 * @author zengcheng
 * @date 2019/4/12
 */
@Component
public class OrderFeignServiceFuseImpl implements OrderFeignService {

    @Override
    public String sayHiFromOrderModule() {
        return "sorry,order module has no response!";
    }
}
