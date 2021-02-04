/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms;

import com.iwindplus.boot.sms.domain.LingkaiSmsProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用凌凯短信服务.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LingkaiSmsConfig.class)
@EnableConfigurationProperties(LingkaiSmsProperty.class)
public @interface EnableLingkaiSms {
}
