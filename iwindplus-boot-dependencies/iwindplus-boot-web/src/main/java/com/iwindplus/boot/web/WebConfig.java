package com.iwindplus.boot.web;/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * web相关配置.
 *
 * @author zengdegui
 * @since 2020年11月8日
 */
@Slf4j
@Configuration
public class WebConfig {

    /**
     * 创建 MultipartResolver.
     *
     * @return MultipartResolver
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        log.info("CommonsMultipartResolver [{}]", multipartResolver);
        return multipartResolver;
    }
}
