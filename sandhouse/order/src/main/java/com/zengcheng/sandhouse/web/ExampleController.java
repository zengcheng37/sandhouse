package com.zengcheng.sandhouse.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 样例控制器
 * @author zengcheng
 * @date 2019/4/11
 */
@RequestMapping("/hello")
@RestController
public class ExampleController {

    @GetMapping("/hi")
    public String sayHi(){
        return "HelloWorld";
    }

}
