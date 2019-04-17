package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.service.OrderFeignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * 管理员用户相关请求控制器
 * @author zengcheng
 * @date 2019/4/12
 */
@RequestMapping("/admin")
@RestController
public class AdminController {

    @Resource
    private OrderFeignService orderFeignService;

    @PostMapping("/login")
    public String loginAdmin(){
        return orderFeignService.sayHiFromOrderModule();
    }

}
