/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2;

import com.iwindplus.cloud.oauth2.domain.Oauth2Property;
import com.iwindplus.cloud.oauth2.exception.CustomAccessDeniedHandler;
import com.iwindplus.cloud.oauth2.exception.CustomAuthenticationEntryPoint;
import com.iwindplus.cloud.oauth2.service.Md5PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * oauth2配置.
 *
 * @author zengdegui
 * @since 2020年11月8日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({Oauth2Property.class})
public class Oauth2Config {
    /**
     * 默认加密配置.
     *
     * @return PasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean(Md5PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new Md5PasswordEncoder();
        log.info("Md5PasswordEncoder [{}]", encoder);
        return encoder;
    }

    /**
     * 创建CustomAccessDeniedHandler.
     *
     * @return CustomAccessDeniedHandler
     */
    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();
        log.info("CustomAccessDeniedHandler [{}]", customAccessDeniedHandler);
        return customAccessDeniedHandler;
    }

    /**
     * 创建CustomAuthenticationEntryPoint.
     *
     * @return CustomAuthenticationEntryPoint
     */
    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
        log.info("CustomAuthenticationEntryPoint [{}]", customAuthenticationEntryPoint);
        return customAuthenticationEntryPoint;
    }
}
