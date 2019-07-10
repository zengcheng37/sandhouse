package com.zengcheng.sandhouse.common.config;

import com.zengcheng.sandhouse.common.filter.AccessTokenFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * SpringSecurity配置
 * @author zengcheng
 * @date 2019/4/15
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    @Qualifier("userDetailService")
    private UserDetailsService userDetailsService;

    @Resource
    private AccessTokenFilter accessTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //禁用csrf
                .csrf().disable()
                //因为使用JWT，所以不需要HttpSession，此处将session生成策略变为无状态
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS).and()
                //认证请求
                .authorizeRequests()
                //swagger相关请求路径放行
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                //错误请求地址放行
                .antMatchers("/error/**").permitAll()
                //OPTIONS请求全部放行
                .antMatchers( HttpMethod.OPTIONS, "/**").permitAll()
                //登录接口放行
                .antMatchers("/admin/login").permitAll()
                //监控接口放行
                .antMatchers("/actuator/**").permitAll()
                //其他接口全部接受验证
                .anyRequest().authenticated()
                .and()
                //对上述匹配成功请求添加过滤器
                .addFilterAfter(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        //开启头部缓存
        http.headers().cacheControl();
    }

    private Filter authenticationTokenFilterBean() {
        return accessTokenFilter;
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //配置自己的UserDetailsService
                .userDetailsService(userDetailsService)
                //配置密码加密方式以及匹配方式
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}
