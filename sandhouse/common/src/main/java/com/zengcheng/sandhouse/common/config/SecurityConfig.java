package com.zengcheng.sandhouse.common.config;

import com.zengcheng.sandhouse.common.filter.AccessTokenFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * SpringSecurity配置
 * @author zengcheng
 * @date 2019/4/15
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({CustomOIDCClientProperties.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccessTokenFilter accessTokenFilter;

    @Resource
    private CustomOIDCClientProperties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //禁用csrf
                .csrf().disable()
                //因为使用JWT，所以不需要HttpSession，此处将session生成策略变为无状态
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
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
                //监控接口放行
                .antMatchers("/actuator/**").permitAll()
                //其他接口全部接受验证
                .anyRequest().authenticated()
                .and()
                //设置oauth2登录页面
                .oauth2Login()
                .loginPage("/login").permitAll().failureUrl("/login?#/error")
                .and()
                //对上述匹配成功请求添加过滤器
                .addFilterAfter(accessTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //开启头部缓存
        http.headers().cacheControl();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.oidcClientRegistration());
    }

    private ClientRegistration oidcClientRegistration() {
        return ClientRegistration.withRegistrationId(properties.getClientId())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate(properties.getRedirectUriTemplate())
                .scope("openid", "profile", "email")
                .authorizationUri(properties.getAuthorizationUri())
                .tokenUri(properties.getTokenUri())
                .userInfoUri(properties.getUserInfoUri())
                .jwkSetUri(properties.getJwkSetUri())
                .userNameAttributeName(properties.getUserNameAttributeName())
                .clientName("SandHouse Login")
                .build();
    }

}
