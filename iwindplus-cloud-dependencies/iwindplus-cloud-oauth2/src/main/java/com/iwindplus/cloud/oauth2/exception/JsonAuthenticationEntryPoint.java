/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.cloud.web.exception.CloudGlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 网关未认证处理.
 *
 * @author zengdegui
 * @since 2020年4月22日
 */
@Slf4j
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Autowired
    private CloudGlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {
        ResponseEntity<ResultVO> result = this.globalExceptionHandler.exception(exception);
        return Mono.defer(() -> {
            return Mono.just(exchange.getResponse());
        }).flatMap((response) -> {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            Mono<? extends Void> buffer = getMono(result, response, dataBufferFactory);
            if (buffer != null) {
                return buffer;
            }
            return null;
        });
    }

    private Mono<? extends Void> getMono(ResponseEntity<ResultVO> result, ServerHttpResponse response,
            DataBufferFactory dataBufferFactory) {
        try {
            DataBuffer buffer = dataBufferFactory.wrap(
                    this.objectMapper.writeValueAsString(result.getBody()).getBytes(Charset.defaultCharset()));
            if (null != buffer) {
                return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
                    DataBufferUtils.release(buffer);
                });
            }
        } catch (JsonProcessingException ex) {
            log.error("Json processing exception [{}]", ex);
        }
        return null;
    }
}
