/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign;

import com.iwindplus.cloud.feign.service.FeignErrorDecoder;
import com.iwindplus.cloud.feign.service.FeignHystrixConcurrencyStrategy;
import com.iwindplus.cloud.feign.service.FeignRequestInterceptor;
import com.iwindplus.cloud.feign.service.FeignSpringFormEncoder;
import feign.Feign;
import feign.Feign.Builder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign配置.
 *
 * @author zengdegui
 * @since 2019/9/3
 */
@Slf4j
@Configuration
public class FeignConfig {
    /**
     * 创建Encoder.
     *
     * @param objectFactory objectFactory
     * @return Encoder
     */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> objectFactory) {
        Encoder encoder = new FeignSpringFormEncoder(new SpringEncoder(objectFactory));
        log.info("FeignSpringFormEncoder [{}]", encoder);
        return encoder;
    }

    /**
     * 创建Decoder.
     *
     * @param objectFactory objectFactory
     * @return Decoder
     */
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> objectFactory) {
        ResponseEntityDecoder decoder = new ResponseEntityDecoder(new SpringDecoder(objectFactory));
        log.info("ResponseEntityDecoder [{}]", decoder);
        return decoder;
    }

    /**
     * 创建feignBuilder.
     *
     * @return Builder
     */
    @Bean
    public Builder feignBuilder() {
        Builder builder = Feign.builder()
                .requestInterceptor(new FeignRequestInterceptor())
                .errorDecoder(new FeignErrorDecoder());
        log.info("FeignBuilder [{}]", builder);
        return builder;
    }

    /**
     * 创建FeignHystrixConcurrencyStrategy.
     *
     * @return FeignHystrixConcurrencyStrategy
     */
    @Bean
    public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
        FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy = new FeignHystrixConcurrencyStrategy();
        log.info("FeignHystrixConcurrencyStrategy [{}]", feignHystrixConcurrencyStrategy);
        return feignHystrixConcurrencyStrategy;
    }
}
