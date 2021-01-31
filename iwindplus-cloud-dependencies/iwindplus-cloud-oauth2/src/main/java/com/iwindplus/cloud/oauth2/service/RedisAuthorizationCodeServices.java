/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.service;

import lombok.Setter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

/**
 * redis授权码模式授权码服务-操作授权码生成、存储、删除.
 *
 * @author zengdegui
 * @since 2020/4/21
 */
@Setter
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
    private static final String AUTHORIZATION_CODE = "authorization_code:";

    private final RedisConnectionFactory connectionFactory;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    /**
     * 授权码有效时长.
     */
    private long expiration = 300L;

    public RedisAuthorizationCodeServices(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    /**
     * 序列化.
     *
     * @param object 对象
     * @return byte[]
     */
    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    /**
     * 反序列化.
     *
     * @param bytes 字节数组
     * @return OAuth2Authentication
     */
    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    /**
     * 将随机生成的授权码存到redis中.
     *
     * @param code           授权码
     * @param authentication 认证信息
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        byte[] serializedKey = serialize(AUTHORIZATION_CODE + code);
        byte[] serializedAuthentication = serialize(authentication);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.set(serializedKey, serializedAuthentication);
            conn.expire(serializedKey, expiration);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    /**
     * 取出授权码并删除授权码(权限码只能用一次，调试时可不删除，code就可多次使用).
     *
     * @param code 授权码
     * @return OAuth2Authentication
     */
    @Override
    protected OAuth2Authentication remove(String code) {
        byte[] serializedKey = serialize(AUTHORIZATION_CODE + code);
        RedisConnection conn = getConnection();
        byte[] bytes;
        try {
            bytes = conn.get(serializedKey);
            if (null != bytes) {
                conn.del(serializedKey);
            }
        } finally {
            conn.close();
        }
        return deserializeAuthentication(bytes);
    }
}
