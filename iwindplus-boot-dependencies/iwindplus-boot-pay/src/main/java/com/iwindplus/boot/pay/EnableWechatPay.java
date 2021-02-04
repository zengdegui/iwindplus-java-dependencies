/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay;

import com.iwindplus.boot.pay.domain.WechatPayProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用微信支付服务.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WechatPayConfig.class)
@EnableConfigurationProperties(WechatPayProperty.class)
public @interface EnableWechatPay {
}
