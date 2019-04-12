package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.service.OrderFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 样例控制器
 * @author zengcheng
 * @date 2019/4/12
 */
@RequestMapping("/hello")
@RestController
public class ExampleController {

    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/hi")
    public String sayHi(){
        return orderFeignService.sayHiFromOrderModule();
    }

}
