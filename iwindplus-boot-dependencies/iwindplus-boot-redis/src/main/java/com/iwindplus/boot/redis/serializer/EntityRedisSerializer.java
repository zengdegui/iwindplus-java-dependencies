/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.redis.serializer;

import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis Object 序列化，反序列化工具.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class EntityRedisSerializer implements RedisSerializer<Object> {
    /**
     * 序列化.
     *
     * @param object 对象
     * @return byte[]
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(Object object) throws SerializationException {
        return new SerializingConverter().convert(object);
    }

    /**
     * 反序列化.
     *
     * @param bytes 字节数组
     * @return Object
     * @throws SerializationException
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return new DeserializingConverter().convert(bytes);
    }
}
