package com.zengcheng.sandhouse.util;

import com.zengcheng.sandhouse.constant.RedisKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 * JWT 工具类
 * @author zengcheng
 * @date 2019/4/14
 */
@Component
public class JwtTokenUtil implements Serializable {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * JWT私钥
     */
    private static final String SECRET = "secret";


    /**
     * 验证JWT是否过期以及是否在redis中存在
     * 1.判断token中的信息过期时间等(此处不做权限校验).
     * 2.判断token是否在redis中.
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
        Boolean ifTokenInRedis = redisTemplate.opsForHash().hasKey(RedisKeys.TOKEN_HASH,token);
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

}
