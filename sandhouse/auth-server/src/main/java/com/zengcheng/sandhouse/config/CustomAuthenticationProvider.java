package com.zengcheng.sandhouse.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义认证服务
 * 参考自 @link:https://segmentfault.com/a/1190000013057238
 * @author zengcheng
 * @date 2019/09/29
 */
@Component("CustomAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    @Qualifier("CustomUserDetailService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authenticate) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token
                = (UsernamePasswordAuthenticationToken) authenticate;
        String username = token.getName();
        UserDetails userDetails = null;

        if(username !=null) {
            userDetails = userDetailsService.loadUserByUsername(username);
        }

        if(userDetails == null) {
            throw new UsernameNotFoundException("用户名/密码无效");
        }

        else if (!userDetails.isEnabled()){
            throw new DisabledException("用户已被禁用");
        }else if (!userDetails.isAccountNonExpired()) {
            throw new LockedException("账号已过期");
        }else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("账号已被锁定");
        }else if (!userDetails.isCredentialsNonExpired()) {
            throw new LockedException("凭证已过期");
        }

        String password = userDetails.getPassword();
        //与authentication里面的credentials相比较
        if(!new BCryptPasswordEncoder().matches((String)token.getCredentials(),password)) {
            throw new BadCredentialsException("用户名/密码无效");
        }
        //授权
        return new UsernamePasswordAuthenticationToken(userDetails, password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

}
