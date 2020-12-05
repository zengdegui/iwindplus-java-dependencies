/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.redis.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis Object 序列化，反序列化工具.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
public class EntityRedisSerializer implements RedisSerializer<Object> {
    private Converter<Object, byte[]> serializer = new SerializingConverter();
    private Converter<byte[], Object> deserializer = new DeserializingConverter();

    /**
     * 序列化.
     *
     * @param object 对象
     * @return byte[]
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(Object object) throws SerializationException {
        return serializer.convert(object);
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
        return deserializer.convert(bytes);
    }
}
