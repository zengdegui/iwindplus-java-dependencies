/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro;

import com.iwindplus.boot.shiro.domain.ShiroProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用shiro无状态服务.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JwtConfig.class)
@EnableConfigurationProperties(ShiroProperty.class)
public @interface EnableShiroJwt {
}
