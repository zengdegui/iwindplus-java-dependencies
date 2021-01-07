/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

import java.io.IOException;

/**
 * 自定义序列化工具
 *
 * @author zengdegui
 * @since 2020年4月1日
 */
@Slf4j
public class JacksonSerializationStrategy extends StandardStringSerializationStrategy {
    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        T tt = null;
        try {
            tt = new ObjectMapper().readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("IOException [{}]", e);
        }
        return tt;
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        try {
            return new ObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
