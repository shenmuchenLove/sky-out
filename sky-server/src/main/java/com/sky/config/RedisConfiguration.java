package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis 配置类
 * 用于配置和初始化 RedisTemplate Bean，设置序列化器以支持字符串 key 和 JSON 格式的 value
 */
@Component
@Configuration
@Slf4j
public class RedisConfiguration {

    /**
     * 创建并配置 RedisTemplate Bean
     * 该模板用于操作 Redis 数据库，支持 String 类型的 key 和 Object 类型的 value
     * key 采用 String 序列化器，value 采用 JSON 序列化器，同时支持 Hash 类型操作
     *
     * @param redisConnectionFactory Redis 连接工厂，由 Spring 容器自动注入，用于建立与 Redis 服务器的连接
     * @return 配置完成的 RedisTemplate 实例，泛型参数为<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("创建 RedisTemplate...");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        /* 设置 Redis 连接工厂 */
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        /* 设置 key 的序列化器为 StringRedisSerializer */
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        /* 设置 value 的序列化器为 GenericJackson2JsonRedisSerializer */
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        /* 设置 hash key 的序列化器为 StringRedisSerializer */
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        /* 设置 hash value 的序列化器为 GenericJackson2JsonRedisSerializer */
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

}
