/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat;

import com.iwindplus.boot.wechat.domain.WechatMpProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用微信公众号服务.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WechatMpConfig.class)
@EnableConfigurationProperties(WechatMpProperty.class)
public @interface EnableWechatMp {
}
