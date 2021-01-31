/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service.manager;

import com.iwindplus.boot.shiro.domain.dto.ShiroCacheDTO;
import lombok.Setter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * redis实现shiro缓存管理器.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Setter
public class RedisCacheManager implements CacheManager {
    @SuppressWarnings("rawtypes")
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 前缀.
     */
    private String keyPrefix = "shiro-cache:";

    /**
     * 失效时间，单位秒.
     */
    private Integer timeout = 7200;

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache<K, V> cache = this.caches.get(name);
        if (null == cache) {
            cache = new ShiroCacheDTO<K, V>(name, (RedisTemplate<K, V>) redisTemplate, keyPrefix, timeout);
            this.caches.put(name, cache);
        }
        return cache;
    }
}
