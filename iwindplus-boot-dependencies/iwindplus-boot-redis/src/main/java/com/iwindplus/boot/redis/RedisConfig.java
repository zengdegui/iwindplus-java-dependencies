/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置.
 *
 * @author zengdegui
 * @since 2018/9/5
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 自定义redis key值生成策略.
     *
     * @return KeyGenerator
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        KeyGenerator keyGenerator = (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append("|");
                sb.append(obj.toString());
            }
            return sb.toString();
        };
        log.info("KeyGenerator [{}]", keyGenerator);
        return keyGenerator;
    }

    /**
     * 创建ReactiveRedisTemplate.
     *
     * @param factory 工厂
     * @return ReactiveRedisTemplate<String, Object>
     */
    @Bean(name = "reactiveRedisTemplate")
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        RedisSerializationContext<String, Object> serializationContext
                = RedisSerializationContext.<String, Object>newSerializationContext().key(stringRedisSerializer)
                .value(jackson2JsonRedisSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();
        ReactiveRedisTemplate<String, Object> reactiveRedisTemplate = new ReactiveRedisTemplate<>(factory,
                serializationContext);
        log.info("ReactiveRedisTemplate [{}]", reactiveRedisTemplate);
        return reactiveRedisTemplate;
    }

    /**
     * 创建RedisTemplate.
     *
     * @param factory 工厂
     * @return RedisTemplate<String, Object>
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setEnableTransactionSupport(true);
        log.info("RedisTemplate [{}]", redisTemplate);
        return redisTemplate;
    }

    /**
     * 创建CacheManager.
     *
     * @param factory 工程
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
        log.info("CacheManager [{}]", cacheManager);
        return cacheManager;
    }

    /**
     * 重写异常捕获，不抛出异常，使得继续访问数据库.
     *
     * @return CacheErrorHandler
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException ee, Cache cache, Object key) {
                log.error("handleCacheGetError [{}]", ee);
            }

            @Override
            public void handleCachePutError(RuntimeException ee, Cache cache, Object key, Object value) {
                log.error("handleCachePutError [{}]", ee);
            }

            @Override
            public void handleCacheEvictError(RuntimeException ee, Cache cache, Object key) {
                log.error("handleCacheEvictError [{}]", ee);
            }

            @Override
            public void handleCacheClearError(RuntimeException ee, Cache cache) {
                log.error("handleCacheClearError [{}]", ee);
            }
        };
        return cacheErrorHandler;
    }
}
