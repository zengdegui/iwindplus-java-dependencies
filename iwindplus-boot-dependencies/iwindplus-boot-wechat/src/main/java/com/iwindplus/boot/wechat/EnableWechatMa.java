/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat;

import com.iwindplus.boot.wechat.domain.WechatMaProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用微信小程序服务.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WechatMaConfig.class)
@EnableConfigurationProperties(WechatMaProperty.class)
public @interface EnableWechatMa {
}