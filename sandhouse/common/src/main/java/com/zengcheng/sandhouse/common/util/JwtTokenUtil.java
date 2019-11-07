package com.zengcheng.sandhouse.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * JWT 工具类
 * @author zengcheng
 * @date 2019/10/25
 */
@Component
@Slf4j
public class JwtTokenUtil {

    /**
     * claims中存储的信息--用户名
     */
    private static final String CLAIM_KEY_USERNAME = "user_name";
    /**
     * claims中存储的信息--权限
     */
    private static final String CLAIM_KEY_AUTHORITIES = "authorities";
    /**
     * JWT私钥
     */
    private static final String SECRET = "sandhouse";

    @Resource
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;


    /**
     * 验证JWT是否过期以及是否在redis中存在
     * 1.判断token是否在redis中.
     * 2.判断token中的信息过期时间等(此处不做权限校验).
     */
    public String validateToken(String token) {
        Map<String,Object> queryParams = new HashMap<>(2);
        queryParams.put("token",token);
        JsonNode tokenResp =
                restTemplate.getForObject("http://SERVICE-AUTH/oauth/check_token?token={token}",JsonNode.class,queryParams);
        if(StringUtils.isEmpty(tokenResp)  || !StringUtils.isEmpty(tokenResp.get("error"))){
            return "invalid accessToken!";
        }else {
            return null;
        }
    }

    /**
     * 获取token是否过期
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken( token );
        return expiration.before( new Date() );
    }

    /**
     * 根据token获取userName
     */
    public String getUserNameFromToken(String token) {
        try{
            //成功解析
            return getClaimsFromToken( token ).getStringClaim(CLAIM_KEY_USERNAME);
        }catch (Exception ex){
            //解析错误
            log.error("JwtTokenUtil token中获取用户名异常!",ex);
            return null;
        }
    }

    /**
     * 根据token获取authorities
     */
    public Set getAuthoritiesFromToken(String token) {
        try{
            //成功解析
            List<String> authorityList = getClaimsFromToken( token ).getStringListClaim(CLAIM_KEY_AUTHORITIES);
            Set<GrantedAuthority> authorities = new HashSet<>();
            for(String authority : authorityList){
                GrantedAuthority tempAuthority = new SimpleGrantedAuthority(authority);
                authorities.add(tempAuthority);
            }
            return authorities;
        }catch (Exception ex){
            //解析错误
            log.error("JwtTokenUtil token中获取权限信息异常!",ex);
            return null;
        }
    }

    /**
     * 获取token的过期时间
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken( token ).getExpirationTime();
    }

    /**
     * 解析JWT
     */
    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claimsSet = null;
        try {
            claimsSet = JWTParser.parse(token).getJWTClaimsSet();
        } catch (ParseException e) {
            log.error("JwtTokenUtil token解析异常!",e);
        }
        return claimsSet;
    }

}
