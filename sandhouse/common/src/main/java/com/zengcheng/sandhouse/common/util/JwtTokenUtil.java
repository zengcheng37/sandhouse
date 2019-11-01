package com.zengcheng.sandhouse.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * JWT 工具类
 * @author zengcheng
 * @date 2019/10/25
 */
@Component
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
                restTemplate.getForObject("/service-auth/oauth/check_token?token={token}",JsonNode.class,queryParams);
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
            return getClaimsFromToken( token ).get(CLAIM_KEY_USERNAME,String.class);
        }catch (Exception ex){
            //解析错误
            return null;
        }
    }

    /**
     * 根据token获取authorities
     */
    public Set getAuthoritiesFromToken(String token) {
        try{
            //成功解析
            return getClaimsFromToken( token ).get(CLAIM_KEY_AUTHORITIES,Set.class);
        }catch (Exception ex){
            //解析错误
            return null;
        }
    }

    /**
     * 获取token的过期时间
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken( token ).getExpiration();
    }

    /**
     * 解析JWT
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey( SECRET )
                .parseClaimsJws( token )
                .getBody();
    }

}
