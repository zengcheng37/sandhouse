package com.zengcheng.sandhouse.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zengcheng.sandhouse.common.util.JwtTokenUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author zengcheng
 * @date 2019/4/18
 */
@Component
public class AccessTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader("accessToken");
        // 如果请求头中没有token信息则直接放行了
        if (tokenHeader == null) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则先进行校验，再设置认证信息
        if(jwtTokenUtil.validateToken(tokenHeader) != null){
            //如果token无效则放行
            chain.doFilter(request,response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        chain.doFilter(request, response);
    }

    /**
     * 这里从token中获取用户信息并新建一个UsernamePasswordAuthenticationToken
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String username = jwtTokenUtil.getUserNameFromToken(tokenHeader);
        if (username != null){
            //从redis中获取该用户角色权限
            String authentications = redisTemplate.opsForValue().get(tokenHeader);
            Set<SimpleGrantedAuthority> grantedAuthorities;
            try{
                grantedAuthorities= JSON.parseObject(authentications,new TypeReference<Set<SimpleGrantedAuthority>>(){});
            }catch (Exception ex){
                return null;
            }
            //新建一个UsernamePasswordAuthenticationToken
            return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        }
        return null;
    }
}
