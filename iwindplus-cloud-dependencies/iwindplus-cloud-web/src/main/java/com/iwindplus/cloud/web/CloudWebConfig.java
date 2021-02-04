/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 默认配置类.
 *
 * @author zengdegui
 * @since 2020/3/31
 */
@Slf4j
@Configuration
public class CloudWebConfig {
    /**
     * 创建RestTemplate.
     *
     * @return RestTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }
}
