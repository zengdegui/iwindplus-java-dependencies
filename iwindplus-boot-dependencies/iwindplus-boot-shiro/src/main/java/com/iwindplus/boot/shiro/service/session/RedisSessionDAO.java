/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 前缀.
     */
    private String keyPrefix = "shiro-session:";

    /**
     * 失效时间，单位秒.
     */
    private Integer timeout = 7200;

    /**
     * 创建session.
     *
     * @param session session
     * @return Serializable
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        String key = this.getKey(sessionId);
        this.redisTemplate.opsForValue().set(key, session, this.timeout, TimeUnit.SECONDS);
        return sessionId;
    }

    /**
     * 获取session.
     *
     * @param sessionId sessionId
     * @return Session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
        if (null == sessionId) {
            return null;
        }
        String key = this.getKey(sessionId);
        Session session = (Session) this.redisTemplate.opsForValue().get(key);
        return session;
    }

    /**
     * 更新session的最后一次访问时间.
     *
     * @param session session
     */
    @Override
    protected void doUpdate(Session session) {
        if (null == session || null == session.getId()) {
            return;
        }
        session.setTimeout(this.timeout.longValue() * 1000);
        String key = this.getKey(session.getId());
        this.redisTemplate.opsForValue().set(key, session, this.timeout, TimeUnit.SECONDS);
    }

    /**
     * 删除session.
     *
     * @param session session
     */
    @Override
    protected void doDelete(Session session) {
        if (null == session) {
            return;
        }
        String key = this.getKey(session.getId());
        this.redisTemplate.delete(key);
    }

    /**
     * 拼装缓存key.
     *
     * @param key 键
     * @return String
     */
    private String getKey(Object key) {
        return new StringBuilder(this.keyPrefix).append(key).toString();
    }
}
