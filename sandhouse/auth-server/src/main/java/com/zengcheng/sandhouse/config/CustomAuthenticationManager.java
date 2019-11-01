package com.zengcheng.sandhouse.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 自定义身份验证管理器
 * @author zengcheng
 * @date 2019/10/25
 */
@Component("CustomAuthenticationManager")
public class CustomAuthenticationManager implements AuthenticationManager {

    @Resource
    @Qualifier("CustomAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = authenticationProvider.authenticate(authentication);
        if (!StringUtils.isEmpty(result)) {
            return result;
        }
        throw new ProviderNotFoundException("身份验证失败");
    }

}
