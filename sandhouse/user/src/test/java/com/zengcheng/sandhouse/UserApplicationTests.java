//package com.zengcheng.sandhouse;
//
//import com.alibaba.fastjson.JSON;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserApplicationTests {
//
//    @Resource
//    @Qualifier("userDetailService")
//    private UserDetailsService userDetailsService;
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Test
//    public void contextLoads() {
//        UserDetails userDetails = userDetailsService.loadUserByUsername("曾诚");
////        System.out.println("JSON解析后的json字符串为:"+JSON.toJSON(userDetails.getAuthorities()));
//        redisTemplate.opsForValue().set("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5pu-6K-aIiwidHlwZSI6IjEiLCJleHAiOjE1NTYwMDc1NDd9.4j9Uv2vQuLtQV_gNKbJOV93769zQ0ey2UbQO2X03URA",JSON.toJSONString(userDetails.getAuthorities()));
//    }
//
//}
