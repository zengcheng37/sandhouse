package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.feign.OrderFeignService;
import com.zengcheng.sandhouse.props.HelloProps;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 样例控制器
 * @author zengcheng
 * @date 2019/4/12
 */
@RequestMapping("/example")
@RestController
public class ExampleController {

    @Resource
    private HelloProps helloProps;

    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/hi")
    @PreAuthorize("hasRole('admin')")
    public String sayHi(){
        return orderFeignService.sayHiFromOrderModule();
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('service')")
    public String sayHello(){
        return "hello,"+helloProps.getName();
    }

}
