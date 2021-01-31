/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.dto;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis实现shiro缓存.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class ShiroCacheDTO<K, V> implements Cache<K, V> {
    private RedisTemplate<K, V> redisTemplate;

    /**
     * 名称.
     */
    private String name;

    /**
     * 前缀.
     */
    private String keyPrefix;

    /**
     * 失效时间，单位秒.
     */
    private Integer timeout;

    public ShiroCacheDTO(String name, RedisTemplate<K, V> redisTemplate, String keyPrefix, Integer timeout) {
        super();
        this.name = name;
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.timeout = timeout;
    }

    @Override
    public void clear() throws CacheException {
        this.redisTemplate.delete(keys());
    }

    @Override
    public V get(K key) throws CacheException {
        this.redisTemplate.boundValueOps(this.getCacheKey(key)).expire(this.timeout, TimeUnit.SECONDS);
        return this.redisTemplate.boundValueOps(this.getCacheKey(key)).get();
    }

    @Override
    public Set<K> keys() {
        return this.redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V old = this.get(key);
        this.redisTemplate.boundValueOps(this.getCacheKey(key)).set(value, this.timeout, TimeUnit.SECONDS);
        return old;
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = this.get(key);
        this.redisTemplate.delete(this.getCacheKey(key));
        return old;
    }

    @Override
    public int size() {
        return this.keys().size();
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(this.get(s));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private K getCacheKey(Object key) {
        // 此处很重要,如果key是登录凭证,那么这是访问用户的授权缓存;将登录凭证转为user对象,返回user的id属性做为hash.
        // key,否则会以user对象做为hash
        // key,这样就不好清除指定用户的缓存了
        StringBuilder sb = new StringBuilder();
        sb.append(this.keyPrefix).append(this.name).append(":");

        if (key instanceof PrincipalCollection) {
            PrincipalCollection pc = (PrincipalCollection) key;
            String data = (String) pc.asList().get(0);
            sb.append(data);
            return (K) (sb.toString());
        }
        sb.append(key);
        return (K) (sb.toString());
    }
}
