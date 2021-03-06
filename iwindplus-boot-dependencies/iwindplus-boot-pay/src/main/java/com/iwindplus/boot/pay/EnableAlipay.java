/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay;

import com.iwindplus.boot.pay.domain.AlipayProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用支付宝支付服务.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AlipayConfig.class)
@EnableConfigurationProperties(AlipayProperty.class)
public @interface EnableAlipay {
}
