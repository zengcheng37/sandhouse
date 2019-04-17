package com.zengcheng.sandhouse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置
 * @author zengcheng
 * @date 2019/4/15
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //禁用csrf
                .csrf().disable()
                //因为使用JWT，所以不需要HttpSession，此处将session生成策略变为无状态
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS).and()
                //认证请求
                .authorizeRequests()
                //OPTIONS请求全部放行
                .antMatchers( HttpMethod.OPTIONS, "/**").permitAll()
                //登录接口放行
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/**").hasIpAddress("localhost")
                //其他接口全部接受验证
                .anyRequest().authenticated();
//                .and()
//                //对上述匹配成功请求添加过滤器
//                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        //开启头部缓存
        http.headers().cacheControl();
    }

}
