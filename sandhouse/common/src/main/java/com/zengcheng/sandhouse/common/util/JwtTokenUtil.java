package com.zengcheng.sandhouse.common.util;

import com.alibaba.fastjson.JSON;
import com.zengcheng.sandhouse.common.enums.RedisKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类
 * @author zengcheng
 * @date 2019/4/14
 */
@Component
public class JwtTokenUtil implements Serializable {
    /**
     * claims中存储的信息--用户名
     */
    private static final String CLAIM_KEY_USERNAME = "name";
    /**
     * claims中存储的信息--用户类型 0-管理员用户 1-普通用户
     */
    private static final String CLAIM_KEY_USERTYPE = "type";
    /**
     * 5天(毫秒)
     */
    private static final long EXPIRATION_TIME = 432000000;
    /**
     * JWT私钥
     */
    private static final String SECRET = "secret";

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 签发JWT
     * 根据用户名userName和用户类型uerType生成过期时间EXPIRATION_TIME的token.
     */
    public String generateToken(String userName, String userType, Collection<? extends GrantedAuthority> grantedAuthorities) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USERNAME,userName);
        claims.put(CLAIM_KEY_USERTYPE,userType);
        //生成token
        String generatedToken = Jwts.builder()
                //设置需要存在token中的信息,用HashMap存储
                .setClaims( claims )
                //设置预计过期时间
                .setExpiration( new Date( Instant.now().toEpochMilli() + EXPIRATION_TIME  ) )
                //设置加密方式及私钥
                .signWith( SignatureAlgorithm.HS256, SECRET )
                //完成
                .compact();
        //放入redis中并设置过期时间为EXPIRATION_TIME毫秒
        redisTemplate.opsForValue().set(generatedToken, JSON.toJSONString(grantedAuthorities));
        redisTemplate.expire(generatedToken,EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        return generatedToken;
    }

    /**
     * 验证JWT是否过期以及是否在redis中存在
     * 1.判断token是否在redis中.
     * 2.判断token中的信息过期时间等(此处不做权限校验).
     */
    public String validateToken(String token) {
        Boolean ifTokenExpired;
        //1.判断token中的信息过期时间等(此处不做权限校验).
        try{
            ifTokenExpired = isTokenExpired( token );
        }catch (Exception ex){
            return "invalid accessToken!";
        }
        if(ifTokenExpired){
            return "accessToken is expired!";
        }
        //2.判断token是否在redis中.
        Boolean ifTokenInRedis = redisTemplate.hasKey(token);
        return ifTokenInRedis ? null:"accessToken is expired!";
    }

    /**
     * 获取token是否过期
     */
    public Boolean isTokenExpired(String token) {
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
     * 根据token获取userType
     */
    public String getUserTypeFromToken(String token) {
        try{
            //成功解析
            return getClaimsFromToken( token ).get(CLAIM_KEY_USERTYPE,String.class);
        }catch (Exception ex){
            //解析错误
            return null;
        }
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
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

    /**
     * 删除redis中的指定token
     * @param token
     * @return
     */
    public Boolean deleteToken(String token){
        return redisTemplate.delete(token);
    }

//    public static void main(String[] args){
//        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        System.out.println("加密后"+jwtTokenUtil.generateToken("曾诚","1"));
//    }

}
