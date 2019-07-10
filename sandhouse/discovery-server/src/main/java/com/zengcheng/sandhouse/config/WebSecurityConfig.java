package com.zengcheng.sandhouse.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringSecurity设置
 * @author zengcheng
 * @date 2019/07/10
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            //禁用跨域保护
        http.csrf().disable()
                //开启http基本认证
                .httpBasic();

    }
}
