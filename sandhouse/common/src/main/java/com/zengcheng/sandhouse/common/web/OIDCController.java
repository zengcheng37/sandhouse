package com.zengcheng.sandhouse.common.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义 "/" 路径 添加用户信息后转发
 * @author zengcheng
 * @date 2019/10/30
 */
@Controller
@Profile("custom")
@Slf4j
public class OIDCController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        return "redirect:/oauth2/authorization/sandhouse";
    }

}