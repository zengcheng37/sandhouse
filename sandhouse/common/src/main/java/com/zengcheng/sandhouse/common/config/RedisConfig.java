package com.zengcheng.sandhouse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author zengcheng
 * @date 2019/4/11
 */
@Configuration
public class RedisConfig {

    @Bean
    @Scope("prototype")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        setSerializer(redisTemplate);
        return redisTemplate;
    }

    /**
     * redis键值序列化方式
     */
    private void setSerializer(RedisTemplate<String, Object> template) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 键值型 键的序列化方式
        template.setKeySerializer(redisSerializer);
        // 键值型 值的序列化方式
        template.setValueSerializer(redisSerializer);
        // 哈希型 键的序列化方式
        template.setHashKeySerializer(redisSerializer);
        // 哈希型 值的序列化方式
        template.setHashValueSerializer(redisSerializer);
    }
}
