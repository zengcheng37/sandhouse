package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.service.OrderFeignService;
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
@RequestMapping("/hello")
@RestController
public class ExampleController {

    @GetMapping("/hi")
    @PreAuthorize("hasRole('admin')")
    public String sayHi(){
        return "hello";
    }

}
