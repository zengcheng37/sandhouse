package com.zengcheng.sandhouse.service;

import com.zengcheng.sandhouse.service.impl.OrderFeignServiceHystrixImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 利用feign访问order模块接口
 * @author zengcheng
 * @date 2019/4/12
 */
@FeignClient(value = "service-order",fallback = OrderFeignServiceHystrixImpl.class)
public interface OrderFeignService {
    /**
     * 利用feign调用order模块sayHi接口
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/hello/hi",method = RequestMethod.GET)
    String sayHiFromOrderModule();

}
