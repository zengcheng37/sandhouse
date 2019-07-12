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
        super.configure(http);
        //禁用跨域保护
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/services/**").permitAll()
                //其他接口全部接受验证
                .anyRequest().authenticated()
                .and()
                //开启http基本认证
                .httpBasic();

    }
}
