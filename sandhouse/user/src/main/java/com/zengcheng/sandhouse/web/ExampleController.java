package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.feign.OrderFeignService;
import com.zengcheng.sandhouse.props.HelloProps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Api(value = "rest请求样例控制器",tags = "ExampleController")
@RequestMapping("/example")
@RestController
public class ExampleController {

    @Resource
    private HelloProps helloProps;

    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/hi")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "rest普通Get请求feign调用api",tags = "/hi")
    public String sayHi(){
        return orderFeignService.sayHiFromOrderModule();
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('service')")
    @ApiOperation(value = "rest普通Get请求api",tags = "/hello")
    public String sayHello(){
        return "hello,"+helloProps.getName();
    }

}
